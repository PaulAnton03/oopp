package client.scenes;

import client.utils.ExceptionHandler;
import com.google.inject.Inject;

import client.utils.ClientUtils;
import client.utils.ServerUtils;
import commons.Card;
import commons.CardList;
import commons.CardTag;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;

public class ListSettingsCtrl implements SceneCtrl {

    private final ExceptionHandler exceptionHandler;

    private final ServerUtils server;
    private final ClientUtils client;
    private final MainCtrl mainCtrl;
    private long cardListId;
    @FXML
    private TextField listTitle;
    @FXML
    private ColorPicker listColor;
    @FXML
    private CheckBox useDefault;

    @Inject
    public ListSettingsCtrl(ExceptionHandler exceptionHandler, ServerUtils server, ClientUtils client, MainCtrl mainCtrl) {
        this.exceptionHandler = exceptionHandler;
        this.client = client;
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void loadData(long cardListId) {
        this.cardListId = cardListId;
        this.listTitle.setText(client.getCardList(cardListId).getTitle());
    }

    public void resetForm() {
        listTitle.setText(client.getCardList(cardListId).getTitle());
    }

    public void saveChanges() {
        if(listTitle.getText().isEmpty()) {
            throw new IllegalStateException("List must have a title");
        }
        CardList cardList = client.getCardList(cardListId);
        cardList.setTitle(listTitle.getText());
        server.updateCardList(cardList);
        client.getBoardCtrl().refresh();
        mainCtrl.showMainView();
    }

    public void deleteList() {
        CardList cardList = client.getCardList(cardListId);
        for(CardTag cardTag : server.getCardTags()){
            for(Card card :  cardList.getCards()){
                if(cardTag.getCard().equals(card)){
                    server.deleteCardTag(cardTag.getId());
                }
            }
        }
        server.deleteCardList(cardListId);
        resetForm();
        mainCtrl.showMainView();
    }

    public void goBack() {
        resetForm();
        mainCtrl.showMainView();
    }

    @Override
    public void revalidate() {
        if (client.getCardListCtrls().containsKey(cardListId)) {
            return;
        }
        mainCtrl.showMainView();
        throw new RuntimeException("Sorry, but the list you were editing has been permanently deleted.");
    }
}
