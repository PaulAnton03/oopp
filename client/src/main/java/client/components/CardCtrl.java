package client.components;

import client.scenes.MainCtrl;
import client.utils.ClientUtils;
import client.utils.Logger;
import client.utils.ServerUtils;
import commons.Card;
import commons.CardList;
import javafx.animation.*;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import javafx.util.Duration;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@EqualsAndHashCode
public class CardCtrl implements Component<Card>, DBEntityCtrl<Card, Card/* TODO: change to TAG */>, Initializable {
    private final MainCtrl mainCtrl;
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
    private TextField titleField;

    @Inject
    public CardCtrl(MainCtrl mainCtrl, ServerUtils serverUtils, ClientUtils client) {
        this.mainCtrl = mainCtrl;
        this.server = serverUtils;
        this.client = client;

//         I figured out these do nothing, but if anybody encounters any problems uncomment this
//         Also notify me if that happens, treat this as a test, Błażej
//
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("Card.fxml"));
//        loader.setController(this);
//        loader.setRoot(this);
    }

    @Override
    public Parent getNode() { return cardView; }

    @Override
    public void loadData(Card card) {
        this.card = card;
        title.setText(card.getTitle());
        description.setText(card.getDescription());
        if (client.getSelectedCardId() == card.getId()) {
            highlight();
            if (client.isEditingCardTitle())
                editTitle();
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

    public void onTitleFieldKeyTyped(KeyEvent e) {
        client.setEditedCardTitle(titleField.getText());
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

    public void replaceChild(Card card /* TODO: Change to Tag tag */) {}

    // CSS class that defines style for the highlighted card
    private static final PseudoClass HIGHLIGHT_PSEUDO_CLASS = PseudoClass.getPseudoClass("highlight");

    public void editTitle() {
        titleField.setDisable(false);
        titleField.setVisible(true);
        titleField.requestFocus();
        if (!client.isEditingCardTitle()) {
            titleField.setText(card.getTitle());
            client.setEditingCardTitle(true);
            client.setEditedCardTitle(card.getTitle());
        } else {
            titleField.setText(client.getEditedCardTitle());
        }
    }

    public void highlight() {
        if (!cardView.getStyleClass().contains("highlight"))
            cardView.getStyleClass().add("highlight");
    }

    public void unhighlight() {
        cardView.getStyleClass().remove("highlight");
        if (client.getSelectedCardId() == card.getId()
            && client.isEditingCardTitle())
            saveTitle();
    }

    public void stopEditTitle() {
        titleField.setDisable(true);
        titleField.setVisible(false);
    }

    public void saveTitle() {
        if (titleField.getText() != null
            && !titleField.getText().isEmpty()
            && !titleField.getText().equals(card.getTitle())) {
            card.setTitle(titleField.getText());
            server.updateCard(card);
        }
        stopEditTitle();
    }

    public void onTitleFieldKeyPressed(KeyEvent e) {
        if (e.getCode() == KeyCode.ENTER) {
            saveTitle();
            client.setEditingCardTitle(false);
        } else if (e.getCode() == KeyCode.ESCAPE) {
            stopEditTitle();
            client.setEditingCardTitle(false);
        }
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
            ft.setToValue(0.6);});
        buttonsVisibilityPT = new ParallelTransition(fts.get(0), fts.get(1));
        // Set button icons and behaviour
        try (var binInputStream = getClass().getResourceAsStream("/client/images/bin.png");
             var editInputStream = getClass().getResourceAsStream("/client/images/edit.png")) {
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
        cardView.setOnMouseEntered(event -> {
            focus();
            client.changeSelection(card.getId());});
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
            CardList cardListStart = card.getCardList();
            CardList cardListEnd = client.getActiveCardList();
            int position;
            try {
                position = (Integer) client.getActiveCardListCtrl().getCardListView().getUserData();
            } catch (Exception e) {
                return;
            }
            cardListStart.removeCard(card);
            cardListEnd.addCardAtPosition(card, position);
            this.card.setCardList(cardListEnd);
            server.updateCardList(cardListStart);
            server.updateCardList(cardListEnd);
            client.getBoardCtrl().refresh();
            event.consume();
        });
        cardView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                editCard();
            }
        });
    }
}
