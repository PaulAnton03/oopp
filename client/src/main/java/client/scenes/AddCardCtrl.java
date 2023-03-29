package client.scenes;

import javax.inject.Inject;

import client.utils.ClientUtils;
import client.utils.ServerUtils;
import commons.Card;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class AddCardCtrl {
    private final ServerUtils server;
    private final ClientUtils client;
    private final MainCtrl mainCtrl;
    private long cardListId;

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

    public void loadData(long cardListId) {
        this.cardListId = cardListId;
    }

    public void ok() {
        Card card = new Card(title.getText(), description.getText());
        card.setCardList(client.getCardList(cardListId));
        server.addCard(card);
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
