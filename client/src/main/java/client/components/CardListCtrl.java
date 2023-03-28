package client.components;

import java.util.HashMap;

import javax.inject.Inject;

import client.scenes.MainCtrl;
import client.utils.ClientUtils;
import client.utils.ComponentFactory;
import client.utils.ServerUtils;
import commons.Card;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.EqualsAndHashCode;
import lombok.Getter;


@EqualsAndHashCode
public class CardListCtrl implements Component<CardList>, DBEntityCtrl<CardList> {
    private final ClientUtils client;
    private final MainCtrl mainCtrl;
    private final ComponentFactory factory;
    private final ServerUtils server;

    @Getter
    private CardList cardList;
    @FXML
    private Text title;
    @FXML
    private VBox cardListView;

    @Inject
    public CardListCtrl(ClientUtils client, MainCtrl mainCtrl, ComponentFactory factory, ServerUtils serverUtils) {
        this.client = client;
        this.mainCtrl = mainCtrl;
        this.factory = factory;
        this.server = serverUtils;
    }

    public Parent getNode() {
        return cardListView.getParent();
    }

    @Override
    public void loadData(CardList cardList) {
        if (this.cardList != null)
            removeChildren();
        this.cardList = cardList;
        title.setText(cardList.getTitle());

        for (Card card : cardList.getCards()) {
            CardCtrl cardCtrl = factory.create(CardCtrl.class, card);
            cardListView.getChildren().add(cardCtrl.getNode());
        }
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
            client.getCardCtrls().get(card.getId()).remove();
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
}
