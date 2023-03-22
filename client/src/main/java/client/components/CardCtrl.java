package client.components;

import client.scenes.MainCtrl;
import commons.Card;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Getter;

import javax.inject.Inject;


public class CardCtrl {
    @Getter
    private VBox cardView;
    @Getter
    private Card card;
    @FXML
    private Text title;
    @FXML
    private Text description;
    @FXML
    private Button editCardButton;

    private final MainCtrl mainCtrl;

    @Inject
    public CardCtrl(MainCtrl mainCtrl) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Card.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        this.mainCtrl = mainCtrl;
    }

    public void loadData(Card card) {
        this.card = card;
        title.setText(card.getTitle());
        description.setText(card.getDescription());
    }

    public void editCard(){
        mainCtrl.showEditCard(this);
    }

}
