package client.components;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.Card;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.inject.Inject;

@EqualsAndHashCode
public class CardCtrl implements Component<Card> {
    private final MainCtrl mainCtrl;
    private final ServerUtils serverUtils;

    @Getter
    private Card card;

    @FXML
    private VBox cardView;
    @FXML
    private Text title;
    @FXML
    private Label description;

    @Inject
    public CardCtrl(MainCtrl mainCtrl, ServerUtils serverUtils) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = serverUtils;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Card.fxml"));
        loader.setController(this);
        loader.setRoot(this);
    }

    @Override
    public Parent getNode() { return cardView; }

    @Override
    public void loadData(Card card) {
        this.card = card;
        title.setText(card.getTitle());
        description.setText(card.getDescription());
    }

    public void editCard() { mainCtrl.showEditCard(this); }

    public void delete() {
        serverUtils.deleteCard(card.getId());
        mainCtrl.showMainView();
    }

    public void highlight() {
        // TODO: change how highlighted card is displayed
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(0.6);
        getNode().setEffect(colorAdjust);
    }

    public void unhighlight() {
        getNode().setEffect(null);
    }

}
