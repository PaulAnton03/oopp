package client.components;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javax.inject.Inject;

import client.scenes.MainCtrl;
import client.utils.ComponentFactory;
import client.utils.ServerUtils;
import commons.Card;
import commons.Tag;
import client.utils.ClientUtils;
import client.utils.Logger;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.Transition;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.FlowPane;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import javafx.util.Duration;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class CardCtrl implements Component<Card>, DBEntityCtrl<Card, Tag>, Initializable {
    private final MainCtrl mainCtrl;
    private final ComponentFactory factory;
    private final ServerUtils server;
    private final ClientUtils client;

    @Getter
    private Card card;
    @FXML
    private VBox cardView;
    @FXML
    private Label title;
    @FXML
    private Label description;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private FlowPane tagArea;

    @Inject
    public CardCtrl(MainCtrl mainCtrl, ServerUtils server, ComponentFactory factory, ClientUtils client) {
        this.mainCtrl = mainCtrl;
        this.factory = factory;
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

        for (Tag tag : card.getTags()){
            TagCtrl tagCtrl = factory.create(TagCtrl.class, tag);
            tagArea.getChildren().add(tagCtrl.getNode());
        }
        if (client.getSelectedCardId() == card.getId()) {
            highlight();
        } else {
            unhighlight();
        }
    }

    public void editCard() {
        if(!client.getBoardCtrl().getBoard().isEditable()) {
            throw new IllegalStateException("You do not have permissions to edit this board.");
        }
        mainCtrl.showEditCard(this.getCard().getId());
    }

    public void delete() {
        if (!client.getBoardCtrl().getBoard().isEditable()) {
            throw new IllegalStateException("You do not have permissions to edit this board.");
        }
        this.card = server.deleteCard(card.getId());
    }

    public void refresh() {
        loadData(server.getCard(card.getId()));
        client.getCardListCtrl(card.getCardList().getId()).replaceChild(card);
    }

    public void remove() {
        client.getCardCtrls().remove(card.getId());
        removeChildren();
    }

    public void removeChildren() {}

    public void replaceChild(Tag tag /* TODO: Change to Tag tag */) {

    }

    // CSS class that defines style for the highlighted card
    private static final PseudoClass HIGHLIGHT_PSEUDO_CLASS = PseudoClass.getPseudoClass("highlight");

    public void highlight() {
        if (!cardView.getStyleClass().contains("highlight"))
            cardView.getStyleClass().add("highlight");
    }

    public void unhighlight() {
        cardView.getStyleClass().remove("highlight");
    }

    public void focus() {
        ScaleTransition st = new ScaleTransition(Duration.millis(100), cardView);
        st.setToX(1.15);
        st.setToY(1.15);
        showButtons();
        st.play();
        cardView.setViewOrder(-1.0);
    }

    public void unfocus() {
        ScaleTransition st = new ScaleTransition(Duration.millis(100), cardView);
        st.setToX(1);
        st.setToY(1);
        st.play();
        hideButtons();
        cardView.setViewOrder(0.0);
    }

    public Transition getOpacityTransition(Node node, double initialOpacity, double finalOpacity, double duration) {
        FadeTransition ft = new FadeTransition(Duration.millis(duration), node);
        ft.setFromValue(initialOpacity);
        ft.setToValue(finalOpacity);
        return ft;
    }

    public void applyButtonHoverStyle(Button button) {
        button.setOnMouseEntered(event -> {
            if (buttonsVisibilityPT.getStatus() == Animation.Status.RUNNING) return;
            getOpacityTransition(button, 0.6, 1.0, 100).play();
        });
        button.setOnMouseExited(event -> {
            if (buttonsVisibilityPT.getStatus() == Animation.Status.RUNNING) return;
            getOpacityTransition(button, 1.0, 0.6, 100).play();
        });
    }

    private ParallelTransition buttonsVisibilityPT;

    public void hideButtons() {
        buttonsVisibilityPT.setRate(-1.0);
        buttonsVisibilityPT.playFrom(buttonsVisibilityPT.getCurrentTime());
        title.setPrefWidth(250);
    }

    public void showButtons() {
        title.setPrefWidth(160);
        buttonsVisibilityPT.setRate(1.0);
        buttonsVisibilityPT.playFrom(buttonsVisibilityPT.getCurrentTime());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Hide buttons, unhighlight card
        unhighlight();
        deleteButton.setOpacity(0.0);
        editButton.setOpacity(0.0);
        // Create show/hide transition for buttons
        final Duration ftDuration = Duration.millis(200);
        final Duration ftDelay = Duration.millis(200);
        final List<FadeTransition> fts = List.of(
                new FadeTransition(ftDuration, deleteButton),
                new FadeTransition(ftDuration, editButton));
        fts.forEach(ft -> {
            ft.setDelay(ftDelay);
            ft.setFromValue(0.0);
            ft.setToValue(0.6);
        });
        buttonsVisibilityPT = new ParallelTransition(fts.get(0), fts.get(1));
        // Set button icons and behaviour
        try (var binInputStream =
                     getClass().getResourceAsStream("/client/images/bin.png");
             var editInputStream =
                     getClass().getResourceAsStream("/client/images/edit.png")) {
            // Hover behaviour
            applyButtonHoverStyle(deleteButton);
            applyButtonHoverStyle(editButton);
            // Button graphic
            ImageView binIcon = new ImageView(new Image(binInputStream));
            ImageView editIcon = new ImageView(new Image(editInputStream));
            binIcon.setFitHeight(40);
            editIcon.setFitHeight(30);
            binIcon.setPreserveRatio(true);
            editIcon.setPreserveRatio(true);
            deleteButton.setGraphic(binIcon);
            editButton.setGraphic(editIcon);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Set card view event handlers
        cardView.setOnMouseEntered(event -> focus());
        cardView.setOnMouseExited(event -> unfocus());

        cardView.setOnDragDetected(event -> {
            Logger.log("Card " + getCard().getTitle() + " drag detected");

            SnapshotParameters params = new SnapshotParameters();
            params.setFill(Color.TRANSPARENT);
            params.setTransform(Transform.scale(0.8, 0.8));
            Dragboard db = cardView.startDragAndDrop(TransferMode.MOVE);
            db.setDragView(cardView.snapshot(params, null), event.getX(), event.getY());
            ClipboardContent content = new ClipboardContent();
            content.putString("Card source text");
            db.setContent(content);
            event.consume();
        });

        cardView.setOnDragDone(event -> {
            delete();
            Card card = new Card();
            card.setDescription(this.card.getDescription());
            card.setTitle(this.card.getTitle());
            card.setCardList(client.getActiveCardList());
            card.setId(0); // Default id to be overridden by JPA
            server.addCardAtPosition(card, (Integer) client.getActiveCardListCtrl().getCardListView().getUserData());
            client.getActiveCardListCtrl().refresh();
            event.consume();
        });

        cardView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                editCard();
            }
        });
    }
}
