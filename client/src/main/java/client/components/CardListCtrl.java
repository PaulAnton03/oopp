package client.components;

import client.scenes.MainCtrl;
import client.utils.ClientUtils;
import commons.Card;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Getter;

import javax.inject.Inject;

public class CardListCtrl {
    private final ClientUtils client;
    private final MainCtrl mainCtrl;

    private CardList cardList;
    @FXML
    private Text title;

    @FXML
    @Getter
    private VBox cardListView;

    @Inject
    public CardListCtrl(ClientUtils client, MainCtrl mainCtrl) {
        this.client = client;
        this.mainCtrl = mainCtrl;
    }

    public void loadData(CardList cardList) {
        this.cardList = cardList;
        title.setText(cardList.getTitle());
        for (Card card : cardList.getCardList()) {
            cardListView.getChildren().add(mainCtrl.createCard(card));
        }
    }

    public void addCard() {
        client.setActiveCardList(cardList);
        mainCtrl.showAddCard();
    }

    public void listSettings() {
        client.setActiveCardList(cardList);
        mainCtrl.showListSettings();
    }
}
