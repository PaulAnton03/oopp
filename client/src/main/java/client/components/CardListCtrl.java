package client.components;

import java.util.HashMap;

import javax.inject.Inject;

import client.scenes.MainCtrl;
import client.utils.ClientUtils;
import client.utils.ComponentFactory;
import commons.Card;
import commons.CardList;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.EqualsAndHashCode;
import lombok.Getter;


@EqualsAndHashCode
public class CardListCtrl implements Component<CardList> {
    private final ClientUtils client;
    private final MainCtrl mainCtrl;
    private final ComponentFactory factory;

    @Getter
    private CardList cardList;
    @Getter
    private HashMap<Long, CardCtrl> cardCtrls = new HashMap<>();
    @FXML
    private Text title;
    @FXML
    private VBox cardListView;

    @Inject
    public CardListCtrl(ClientUtils client, MainCtrl mainCtrl, ComponentFactory factory) {
        this.client = client;
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
