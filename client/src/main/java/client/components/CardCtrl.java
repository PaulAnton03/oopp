package client.components;

import commons.Card;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
public class CardCtrl implements Component<Card> {
    @Getter
    private Card card;

    @FXML
    private VBox cardView;
    @FXML
    private Text title;
    @FXML
    private Text description;

    public CardCtrl() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Card.fxml"));
        loader.setController(this);
        loader.setRoot(this);
    }

    public Parent getNode() {
        return cardView;
    }

    @Override
    public void loadData(Card card) {
        this.card = card;
        title.setText(card.getTitle());
        description.setText(card.getDescription());
    }
}
