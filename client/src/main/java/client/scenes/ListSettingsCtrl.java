package client.scenes;

import client.utils.ExceptionHandler;
import com.google.inject.Inject;

import client.utils.ClientUtils;
import client.utils.ServerUtils;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import org.springframework.http.server.DelegatingServerHttpResponse;

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
        listTitle.setText("");
    }

    public void saveChanges() {
        CardList cardList = client.getCardList(cardListId);
        String color;
        if(useDefault.isSelected())
            color = "#b2b2ebff";
        else
            color = mainCtrl.turnColorIntoString(listColor.getValue());
        if(color.equals(client.getBoardCtrl().getBoard().getColor())) {
            throw new IllegalStateException("List color cannot be the same as board color. Please select a different color");
        }
        cardList.setTitle(listTitle.getText());
        cardList.setColor(color);
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

    @Override
    public void revalidate() {
        if (client.getCardListCtrls().containsKey(cardListId)) {
            return;
        }
        mainCtrl.showMainView();
        throw new RuntimeException("Sorry, but the list you were editing has been permanently deleted.");
    }
}
