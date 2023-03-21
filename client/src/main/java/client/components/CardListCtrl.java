package client.components;

import client.scenes.MainCtrl;
import commons.Card;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Getter;

import javax.inject.Inject;

public class CardListCtrl {
    private final MainCtrl mainCtrl;

    private CardList cardList;
    @FXML
    private Text title;

    @FXML
    @Getter
    private VBox cardListView;

    @Inject
    public CardListCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    public void loadData(CardList cardList) {
        this.cardList = cardList;
        title.setText(cardList.getTitle());
        for(Card card : cardList.getCardList())
            cardListView.getChildren().add(mainCtrl.createCard(card));
    }
}
