package client.scenes;

import com.google.inject.Inject;

import client.utils.ClientUtils;
import client.utils.ServerUtils;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ListSettingsCtrl {

    private final ServerUtils server;
    private final ClientUtils client;
    private final MainCtrl mainCtrl;
    private long cardListId;
    @FXML
    private TextField listTitle;

    @Inject
    public ListSettingsCtrl(ServerUtils server, ClientUtils client, MainCtrl mainCtrl) {
        this.client = client;
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void loadData(long cardListId) {
        this.cardListId = cardListId;
    }

    public void resetForm() {
        listTitle.setText("");
    }

    public void saveChanges() {
        CardList cardList = client.getCardList(cardListId);
        cardList.setTitle(listTitle.getText());
        server.updateCardList(cardList);
        client.getBoardCtrl().refresh();
        mainCtrl.showMainView();
    }

    public void deleteList() {
        server.deleteCardList(cardListId);
        resetForm();
        mainCtrl.showMainView();
    }

    public void goBack() {
        resetForm();
        mainCtrl.showMainView();
    }

}
