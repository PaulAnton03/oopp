package client.scenes;

import client.utils.ClientUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ListSettingsCtrl {

    private final ServerUtils server;
    private final ClientUtils client;
    private final MainCtrl mainCtrl;
    @FXML
    private TextField listTitle;

    @Inject
    public ListSettingsCtrl(ServerUtils server, ClientUtils client, MainCtrl mainCtrl) {
        this.client = client;
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public CardList getCardList() {
        CardList cardList = client.getActiveCardList();
        if(cardList == null) {
            throw new IllegalStateException("No card list selected");
        }
        return cardList;
    }

    public void resetForm() {
        listTitle.setText("");
    }
    public void saveChanges() {
        CardList cardList = getCardList();
        cardList.setTitle(listTitle.getText());
        //Todo - figure out a way to update an object in the database
        server.deleteCardList(cardList.getId());
        server.addCardList(cardList);
        mainCtrl.showMainView();
    }

    public void deleteList() {
        server.deleteCardList(getCardList().getId());
        resetForm();
        mainCtrl.showMainView();
    }

    public void goBack() {
        resetForm();
        mainCtrl.showMainView();
    }

}
