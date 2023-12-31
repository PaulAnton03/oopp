package client.components;

import client.scenes.MainCtrl;
import client.utils.ClientUtils;
import client.utils.ComponentFactory;
import client.utils.Logger;
import client.utils.ServerUtils;
import commons.*;
import javafx.animation.*;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import javafx.util.Duration;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
    @Getter
    @FXML
    private TextField titleField;

    @FXML
    private Label finishedSubTasks;

    @FXML
    private Label subTasksCount;

    @Getter
    @Setter
    @FXML
    private FlowPane tagArea;

    @Inject
    public CardCtrl(MainCtrl mainCtrl, ServerUtils server, ClientUtils client,
                    ComponentFactory factory) {
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
        if (tagArea.getChildren() != null)
            tagArea.getChildren().clear();
        description.setText(card.getDescription());
        client.getCardListCtrl(card.getCardList().getId()).replaceChild(card);
        for (CardTag cardTag : server.getCardTags()) {
            System.out.println(cardTag.toString());
            if (cardTag.getCard().equals(card)) {
                Tag tag = cardTag.getTag();
                TagCtrl tagCtrl = factory.create(TagCtrl.class, tag);
                tagCtrl.loadData(tag);
                tagArea.getChildren().add(tagCtrl.getNode());
            }
        }
        if (client.getSelectedCardId() == card.getId()) {
            highlight();
            if (client.getEditedCardTitle() != null)
                editTitle();
        } else {
            unhighlight();
        }
        cardView.setStyle("-fx-background-color: " + card.getCardList().getBoard().getCardColor());
        title.setStyle("-fx-text-fill: " + card.getCardList().getBoard().getFontColor());
        description.setStyle("-fx-text-fill: " + card.getCardList().getBoard().getFontColor());
        subTasksCount.setText(String.valueOf(card.getSubtasks().size()));
        if (card.getSubtasks().size() == 0) {
            finishedSubTasks.setText("0");
            return;
        }
        finishedSubTasks.setText(String.valueOf(card.getSubtasks().stream()
                .filter(SubTask::getFinished).count()));
    }

    public void editCard() {
        if (!client.getBoardCtrl().getBoard().isEditable()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setHeaderText("You do not have permissions to edit this board.");
            alert.showAndWait();
        } else {
            mainCtrl.showEditCard(this.getCard().getId());
        }
    }

    public void delete() {
        if (!client.getBoardCtrl().getBoard().isEditable()) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setHeaderText("You do not have permissions to edit this board.");
            alert.showAndWait();
        } else {
            for(CardTag cardTag : server.getCardTags()) {
                if (cardTag.getCard().equals(card)) {
                    server.deleteCardTag(cardTag.getId());
                }
            }
            this.card = server.deleteCard(card.getId());
        }

    }//with this we first delete the relation with cardTag



    public void refresh() {
        loadData(server.getCard(card.getId()));
    }

    public void remove() {
        client.getCardCtrls().remove(card.getId());
        removeChildren();
    }

    public void removeChildren() {
    }

    public void replaceChild(Tag tag) {
    }

    // CSS class that defines style for the highlighted card
    private static final PseudoClass HIGHLIGHT_PSEUDO_CLASS = PseudoClass.getPseudoClass("highlight");

    public void highlight() {
        if (!cardView.getStyleClass().contains("highlight"))
            cardView.getStyleClass().add("highlight");
    }

    public void unhighlight() {
        cardView.getStyleClass().remove("highlight");
        if (client.getSelectedCardId() == card.getId()
            && client.getEditedCardTitle() != null)
            stopEditTitle();
    }

    public void editTitle() {
        titleField.setDisable(false);
        titleField.setVisible(true);
        title.setVisible(false);
        titleField.requestFocus();
        if (client.getEditedCardTitle() == null) {
            client.setEditedCardTitle(card.getTitle());
            client.setCaretPosition(card.getTitle().length());
        }
        titleField.setText(client.getEditedCardTitle());
        titleField.positionCaret(client.getCaretPosition());
    }

    public void stopEditTitle() {
        titleField.setDisable(true);
        titleField.setVisible(false);
        title.setVisible(true);
        client.setEditedCardTitle(null);
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

    public void onTitleFieldKeyTyped(KeyEvent e) {
        char c = e.getCharacter().charAt(0);
        if (c == 033) { /* ESC */
            stopEditTitle();
        } else if (c == 015) { /* CR (ENTER) */
            saveTitle();
        } else if (StringUtil.isPrintableChar(c)) {
            client.setEditedCardTitle(titleField.getText());
        }
        client.setCaretPosition(titleField.getCaretPosition());
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

    public Transition getOpacityTransition(Node node, double initialOpacity,
                                           double finalOpacity, double duration) {
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
        deleteButton.setOpacity(0.0); editButton.setOpacity(0.0);
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
        try (var binInputStream =
                     getClass().getResourceAsStream("/client/images/bin.png");
             var editInputStream =
                     getClass().getResourceAsStream("/client/images/edit.png")) {
            applyButtonHoverStyle(deleteButton);
            applyButtonHoverStyle(editButton);
            ImageView binIcon = new ImageView(new Image(binInputStream));
            ImageView editIcon = new ImageView(new Image(editInputStream));
            binIcon.setFitHeight(40); editIcon.setFitHeight(30);
            binIcon.setPreserveRatio(true); editIcon.setPreserveRatio(true);
            deleteButton.setGraphic(binIcon); editButton.setGraphic(editIcon);
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
            if (!client.getBoardCtrl().getBoard().isEditable()) {
                Alert alert = new Alert(AlertType.WARNING);
                alert.setHeaderText("You do not have permissions to edit this board.");
                alert.showAndWait();
            } else {
                CardList cardListStart = card.getCardList();
                CardList cardListEnd = client.getActiveCardList();
                int position;
                try {
                    position = (Integer) client.getActiveCardListCtrl().getCardListView().getUserData();
                } catch (Exception e) {
                    return;
                }
                moveCardPosition(cardListStart, cardListEnd, position);
                event.consume();
            }
        });
        cardView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                editCard();
            }
        });
    }

    private void moveCardPosition(CardList cardListStart, CardList cardListEnd, int position) {
        List<CardTag> cardTags = new ArrayList<>();
        for(CardTag cardTag : server.getCardTags()){
            if(cardTag.getCard().equals(card)){
                cardTags.add(cardTag);
                server.deleteCardTag(cardTag.getId());
            }
        }
        cardListStart.removeCard(card);
        cardListEnd.addCardAtPosition(card, position);
        this.card.setCardList(cardListEnd);
        server.updateCardList(cardListStart);
        server.updateCardList(cardListEnd);
        Card card = server.getCard(server.getCardList(cardListEnd.getId()).getCards().get(position).getId());
        for(CardTag cardTag : cardTags){
            cardTag.setCard(card);
            server.createCardTag(cardTag, client.getBoardCtrl().getBoard().getId());
        }
        client.getBoardCtrl().refresh();
    }
}
