package client.components;

import commons.Card;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.Getter;


public class CardCtrl {
    @Getter
    private VBox cardView;
    private Card card;
    @FXML
    private Text title;
    @FXML
    private Text description;

    public CardCtrl() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Card.fxml"));
        loader.setController(this);
        loader.setRoot(this);
    }

    public void loadData(Card card) {
        this.card = card;
        title.setText(card.getTitle());
        description.setText(card.getDescription());
    }
}
