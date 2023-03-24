package client.scenes;

import client.utils.ClientUtils;
import client.utils.ServerUtils;
import commons.Card;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class AddCardCtrl {
    private final ServerUtils server;
    private final ClientUtils client;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField title;
    @FXML
    private TextArea description;

    @Inject
    public AddCardCtrl(ServerUtils server, ClientUtils client, MainCtrl mainCtrl) {
        this.server = server;
        this.client = client;
        this.mainCtrl = mainCtrl;
    }

    private Card getCard() {
        Card newCard = new Card(title.getText(), description.getText());
        newCard.setCardList(client.getActiveCardList());
        if (newCard.getCardList() == null) {
            throw new IllegalStateException("There is no card list specified");
        }
        return newCard;
    }

    public void ok() {
        Card card = getCard();
        server.addCard(card);
        server.send("/app/cards", card);
        goBack();
    }

    public void clearForm() {
        title.setText("");
        description.setText("");
    }

    public void goBack() {
        clearForm();
        mainCtrl.showMainView();
    }
}
