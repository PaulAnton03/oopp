package client.scenes;

import javax.inject.Inject;

import client.utils.ClientUtils;
import client.utils.ServerUtils;
import commons.Card;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class EditCardCtrl {
    private final ServerUtils server;
    private final ClientUtils client;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField changeTitle;
    @FXML
    private TextArea changeDesc;

    @Inject
    public EditCardCtrl(ServerUtils server, ClientUtils client, MainCtrl mainCtrl) {
        this.server = server;
        this.client = client;
        this.mainCtrl = mainCtrl;
    }

    public void saveCardChanges(){
        //todo update the card instead of deleting and adding again
        final Card card = client.getActiveCardCtrl().getCard();

        server.deleteCard(card.getId());
        card.setTitle(changeTitle.getText());
        card.setDescription(changeDesc.getText());
        server.addCard(card);
        client.getActiveCardCtrl().loadData(card);
        mainCtrl.showMainView();
    }

    public void cancel() {
        changeTitle.setText("");
        changeDesc.setText("");
        mainCtrl.showMainView();
    }

    @FXML
    public void onKeyPressed(KeyEvent e) {
        if (KeyCode.ESCAPE == e.getCode()) {
            cancel();
        }
    }
}
