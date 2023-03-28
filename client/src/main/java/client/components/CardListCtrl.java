package client.components;

import client.scenes.MainCtrl;
import client.utils.ClientUtils;
import client.utils.ComponentFactory;
import client.utils.Logger;
import client.utils.ServerUtils;
import commons.Card;
import commons.CardList;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Pair;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.inject.Inject;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;


@EqualsAndHashCode
public class CardListCtrl implements Component<CardList>, Initializable {
    private final ClientUtils client;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final ComponentFactory factory;

    @Getter
    private CardList cardList;
    @Getter
    private HashMap<Long, CardCtrl> cardCtrls = new HashMap<>();
    @FXML
    private Text title;
    @Getter
    @FXML
    private VBox cardListView;

    @Inject
    public CardListCtrl(ClientUtils client, ServerUtils server, MainCtrl mainCtrl, ComponentFactory factory) {
        this.client = client;
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.factory = factory;
    }

    public Parent getNode() {
        return cardListView.getParent();
    }

    @Override
    public void loadData(CardList cardList) {
        this.cardList = cardList;
        title.setText(cardList.getTitle());

        for (Card card : cardList.getCards()) {
            CardCtrl cardCtrl = factory.create(CardCtrl.class, card);
            cardCtrls.put(cardCtrl.getCard().getId(), cardCtrl);
            cardListView.getChildren().add(cardCtrl.getNode());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cardListView.setOnDragEntered(event -> {
            Logger.log("Entered drag");
        });

        cardListView.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.MOVE);
            event.consume();
        });

        cardListView.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            Logger.log("Dropped on list " + this.getCardList().getTitle());
            client.setActiveCardListCtrl(this);

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
            // Logger.log("Inserted at position: ")

            event.setDropCompleted(true);
            event.consume();
        });
    }

    public void refresh() {
        cardListView.getChildren().clear();
        try {
            loadData(server.getCardList(cardList.getId()));
        } catch (Exception e) {
            Logger.log("Card list couldn't be refreshed");
        }

    }

    public void addCard() {
        client.setActiveCardListCtrl(this);
        mainCtrl.showAddCard();
    }

    public void listSettings() {
        client.setActiveCardListCtrl(this);
        mainCtrl.showListSettings();
    }

    /**
     * This method puts a new CardCtrl for the added card
     * in the cardCtrls map and adds it to the interface
     *
     * @param card the new card that needs to be displayed for the user
     */
    public void displayCard(Card card) {
        CardCtrl cardCtrl = factory.create(CardCtrl.class, card);
        cardCtrls.put(card.getId(), cardCtrl);
        Platform.runLater(() -> {
            cardListView.getChildren().add(cardCtrl.getNode());
        });
    }

    /**
     * This method removes the deleted card's CardCtrl from the cardCtrls map
     * and removes it from the interface
     *
     * @param card the card that needs to be deleted from the user's display
     */
    public void removeCard(Card card) {
        CardCtrl cardCtrl = cardCtrls.get(card.getId());
        if (cardCtrl != null) {
            cardCtrls.remove(card.getId());
            Platform.runLater(() -> {
                cardListView.getChildren().remove(cardCtrl.getNode());
            });
        }
    }
}
