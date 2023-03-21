package client.components;

import client.scenes.MainCtrl;
import client.scenes.MainViewCtrl;
import commons.Card;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Getter;

import javax.inject.Inject;
import java.util.List;

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
        for(Card card : cardList.getCardList()) {
            var pair = mainCtrl.createNewCard();
            CardCtrl cardCtrl = pair.getKey();
            var newCard = pair.getValue();
            cardCtrl.loadData(card);
            cardListView.getChildren().add(newCard);
        }
    }
}
