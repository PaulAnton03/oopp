package client.components;

import client.scenes.MainCtrl;
import client.utils.ClientUtils;
import client.utils.Logger;
import client.utils.ServerUtils;
import commons.Card;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.transform.Transform;
import javafx.util.Duration;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

@EqualsAndHashCode
public class CardCtrl implements Component<Card>, Initializable {
    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private final ClientUtils client;

    @Getter
    private Card card;

    @FXML
    private VBox cardView;
    @FXML
    private Text title;
    @FXML
    private Text description;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;

    @Inject
    public CardCtrl(MainCtrl mainCtrl, ServerUtils server, ClientUtils client) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.client = client;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Card.fxml"));
        loader.setController(this);
        loader.setRoot(this);
    }

    @Override
    public Parent getNode() { return cardView; }

    @Override
    public void loadData(Card card) {
        this.card = card;
        title.setText(card.getTitle());
        description.setText(card.getDescription());
    }

    public void editCard() { mainCtrl.showEditCard(this); }

    public void delete() {
        this.card = server.deleteCard(card.getId());
        ((VBox) cardView.getParent()).getChildren().remove(cardView);
    }

    public void highlight() {
        // TODO: change how highlighted card is displayed
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.6);
        getNode().setEffect(colorAdjust);
    }

    public void unhighlight() {
        getNode().setEffect(null);
    }

    public void setButtonsOpacity(double opacity) {
        final double transitionTime = 100;
        final Consumer<Node> showNode = (node) -> {
            FadeTransition ft = new FadeTransition(Duration.millis(transitionTime), node);
            ft.setToValue(opacity);
            ft.play();
        };
        showNode.accept(deleteButton);
        showNode.accept(editButton);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set button icons
        try (var binInputStream = getClass().getResourceAsStream("/client/images/bin.png");
             var editInputStream = getClass().getResourceAsStream("/client/images/edit.png")) {
            ImageView binIcon = new ImageView(new Image(binInputStream));
            ImageView editIcon = new ImageView(new Image(editInputStream));
            binIcon.setFitHeight(40);
            editIcon.setFitHeight(30);
            binIcon.setPreserveRatio(true);
            editIcon.setPreserveRatio(true);
            deleteButton.setGraphic(binIcon);
            editButton.setGraphic(editIcon);
            setButtonsOpacity(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        cardView.setOnMouseEntered(event -> {
            if (cardView.getScaleX() != 1) return;

            final double zoomScaleFactor = 1.15;
            final double transitionTime = 100;

            ScaleTransition st = new ScaleTransition(Duration.millis(transitionTime), cardView);
            st.setToX(zoomScaleFactor);
            st.setToY(zoomScaleFactor);
            setButtonsOpacity(0.6);
            st.play();

        });

        cardView.setOnMouseExited(event -> {
            final double transitionTime = 100;

            ScaleTransition st = new ScaleTransition(Duration.millis(transitionTime), cardView);
            st.setToX(1);
            st.setToY(1);
            st.play();
            setButtonsOpacity(0);
        });

        cardView.setOnDragDetected(event -> {
            Logger.log("Card " + getCard().getTitle() + " drag detected");

            Dragboard db = cardView.startDragAndDrop(TransferMode.ANY);
            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);
            params.setTransform(Transform.scale(0.8, 0.8));
            db.setDragView(cardView.snapshot(params, null), event.getX(), event.getY());
            ClipboardContent content = new ClipboardContent();
            content.putString("Card source text");
            db.setContent(content);
        });

        cardView.setOnDragDone(event -> {
            // TODO replace this with updating the card instead of deleting and adding
            delete();
            getCard().setCardList(client.getActiveCardList());
            getCard().setId(0);
            server.addCardAtPosition(getCard(), (Integer) client.getActiveCardListCtrl().getCardListView().getUserData());
            client.getActiveCardListCtrl().refresh();
        });
    }
}
