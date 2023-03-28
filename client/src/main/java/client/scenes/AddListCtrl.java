package client.scenes;

import client.utils.ClientUtils;
import client.utils.ServerUtils;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class AddListCtrl {

    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private final ClientUtils client;
    @FXML
    private TextField title;

    @Inject
    public AddListCtrl(MainCtrl mainCtrl, ServerUtils server, ClientUtils client) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.client = client;
    }

    public CardList getList() {
        CardList cardList = new CardList(title.getText());
        cardList.setBoard(client.getActiveBoard());
        if (cardList.getBoard() == null) {
            throw new IllegalStateException("No board selected");
        }
        return cardList;
    }

    @FXML
    void addList() {
        CardList cardList = getList();
        server.addCardList(cardList);
        client.getActiveBoardCtrl().refresh(); // TODO: WEBSOCKET
        goBack();
    }

    @FXML
    void clearForm() {
        title.setText("");
    }

    @FXML
    void goBack() {
        clearForm();
        mainCtrl.showMainView();
    }

}
