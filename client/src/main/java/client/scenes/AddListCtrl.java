package client.scenes;

import javax.inject.Inject;

import client.utils.ClientUtils;
import client.utils.ServerUtils;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;

public class AddListCtrl implements SceneCtrl {

    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private final ClientUtils client;

    @FXML private TextField title;
    @FXML private ColorPicker listColor;
    @FXML private CheckBox useDefault;

    @Inject
    public AddListCtrl(MainCtrl mainCtrl, ServerUtils server, ClientUtils client) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.client = client;
    }

    public CardList getList() {
        CardList cardList = new CardList(title.getText());
        String color = useDefault.isSelected() ? cardList.getDefaultColor() : mainCtrl.turnColorIntoString(listColor.getValue());
        if(color.equals(client.getBoardCtrl().getBoard().getColor())) {
            throw new IllegalStateException("List color cannot be the same as board color. Please select a different color");
        }
        cardList.setColor(color);
        cardList.setBoard(client.getBoardCtrl().getBoard());
        if (cardList.getBoard() == null) {
            throw new IllegalStateException("No board selected");
        }
        return cardList;
    }

    @FXML
    void addList() {
        CardList cardList = getList();
        server.addCardList(cardList);
        client.getBoardCtrl().refresh();
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
