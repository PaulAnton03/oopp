package client.components;

import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class CardListCtrl {
    private CardList cardList;
    @FXML
    private Text title;
    @FXML
    private HBox cardsView;

    public void loadData(CardList cardList) {
        // load card lists here
    }
}
