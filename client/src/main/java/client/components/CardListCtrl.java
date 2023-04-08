package client.components;

import client.scenes.MainCtrl;
import client.utils.ClientUtils;
import client.utils.ComponentFactory;
import client.utils.Logger;
import client.utils.ServerUtils;
import commons.Card;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

@EqualsAndHashCode
public class CardListCtrl implements Component<CardList>, DBEntityCtrl<CardList, Card>, Initializable {
    private final ClientUtils client;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final ComponentFactory factory;

    @Getter
    private CardList cardList;
    @FXML
    private Label title;
    @Getter
    @FXML
    private VBox cardListView;
    @FXML
    private Button addCardButton;
    @FXML
    private Button listSettingsButton;
    @FXML
    private AnchorPane cardListBackground;

    @FXML
    private ScrollPane scrollPane;

    @Inject
    public CardListCtrl(ClientUtils client, ServerUtils server, MainCtrl mainCtrl, ComponentFactory factory) {
        this.client = client;
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.factory = factory;
    }

    public Parent getNode() {
        return cardListBackground;
    }

    @Override
    public void loadData(CardList cardList) {
        if (this.cardList != null)
            removeChildren();
        this.cardList = cardList;
        title.setText(cardList.getTitle());
        client.getBoardCtrl().replaceChild(cardList);
        cardListBackground.setStyle("-fx-background-color: " + cardList.getBoard().getListColor());
        // scrollPane.setStyle("-fx-background-color: " + cardList.getBoard().getListColor());
        // cardListView.setStyle("-fx-background-color: " + cardList.getBoard().getListColor());

        for (Card card : cardList.getCards()) {
            CardCtrl cardCtrl = factory.create(CardCtrl.class, card);
            cardListView.getChildren().add(cardCtrl.getNode());
        }

        title.setStyle("-fx-text-fill: " + cardList.getBoard().getFontColor());
    }

    public void refresh() {
        cardListView.getChildren().clear();
        loadData(server.getCardList(cardList.getId()));
    }

    public void remove() {
        removeChildren();
        client.getCardListCtrls().remove(cardList.getId());
    }

    public void removeChildren() {
        for (Card card : this.cardList.getCards()) {
            client.getCardCtrl(card.getId()).remove();
        }
    }

    public void replaceChild(Card card) {
        int idx = IntStream.range(0, cardList.getCards().size())
                .filter(i -> cardList.getCards().get(i).getId() == card.getId())
                .findFirst()
                .orElse(-1);
        if (idx == -1)
            throw new IllegalStateException("Attempting to replace card in card list that does not already exist.");
        cardList.getCards().set(idx, card);
        card.setCardList(cardList);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set button icons and behaviour
        try (var addInputStream = getClass().getResourceAsStream("/client/images/add.png");
             var settingsInputStream = getClass().getResourceAsStream("/client/images/settings.png")) {
            // Button graphic
            ImageView addIcon = new ImageView(new Image(addInputStream));
            ImageView settingsIcon = new ImageView(new Image(settingsInputStream));
            addIcon.setFitHeight(38);
            settingsIcon.setFitHeight(38);
            addIcon.setPreserveRatio(true);
            settingsIcon.setPreserveRatio(true);
            addCardButton.setGraphic(addIcon);
            listSettingsButton.setGraphic(settingsIcon);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        cardListView.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.MOVE);
            event.consume();
        });

        cardListView.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            Logger.log("Dropped on list " + this.getCardList().getTitle());
            client.setActiveCardListId(cardList.getId());

            final double eventY = event.getY();
            // Filter out the currently dragged card
            final List<Node> nodes = cardListView.getChildren().filtered(node -> !node.equals(event.getGestureSource()));
            Pair<Integer, Double> nearestPositionDistance = new Pair<>(nodes.size(), cardListView.getBoundsInLocal().getMaxY() - eventY);
            boolean isPositionAbove = false;

            // Find the position to insert the card
            for (int i = 0; i < nodes.size(); i++) {
                final Node node = nodes.get(i);
                final double signedDistance = eventY - node.getBoundsInParent().getCenterY();
                if (Math.abs(signedDistance) < nearestPositionDistance.getValue()) {
                    nearestPositionDistance = new Pair<>(i, Math.abs(signedDistance));
                    isPositionAbove = signedDistance > 0;
                }
            }

            ((Node) event.getSource()).setUserData(Math.min(nodes.size(),
                    nearestPositionDistance.getKey() + (isPositionAbove ? 1 : 0)));
            Logger.log("Position: " + nearestPositionDistance.getKey());
            Logger.log("Distance: " + nearestPositionDistance.getValue());
            Logger.log("Is above? " + isPositionAbove);

            event.setDropCompleted(true);
            event.consume();
        });
    }

    public void addCard() {
        if (!client.getBoardCtrl().getBoard().isEditable()) {
            throw new IllegalStateException("You do not have permissions to edit this board.");
        }
        mainCtrl.showAddCard(cardList.getId());
    }

    public void listSettings() {
        if (!client.getBoardCtrl().getBoard().isEditable()) {
            throw new IllegalStateException("You do not have permissions to edit this board.");
        }
        mainCtrl.showListSettings(cardList.getId());
    }

}
