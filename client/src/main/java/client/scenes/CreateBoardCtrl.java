package client.scenes;

import client.components.BoardCtrl;
import client.utils.*;
import com.google.inject.Inject;
import commons.Board;
import commons.CardList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class CreateBoardCtrl implements SceneCtrl {

    private final ServerUtils server;
    private final ClientUtils client;
    private final MainCtrl mainCtrl;
    private final ComponentFactory factory;
    private final ClientPreferences clientPrefs;

    private final ThemeUtils themeUtils;

    @FXML
    private TextField boardName;
    @FXML
    private ColorPicker boardColor;
    @FXML
    private TextField boardPassword;
    @FXML
    private CheckBox passwordUsed;

    private Color color;

    @Inject
    public CreateBoardCtrl(ServerUtils server, ClientUtils client, MainCtrl mainCtrl,
                           ComponentFactory factory, ClientPreferences clientPrefs, ThemeUtils themeUtils) {
        this.client = client;
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.factory = factory;
        this.clientPrefs = clientPrefs;
        this.themeUtils = themeUtils;
    }

    public void createBoard() {
        String color = themeUtils.turnColorIntoString(boardColor.getValue());
        final Board newBoard = new Board(boardName.getText());
        if (passwordUsed.isSelected()) {
            newBoard.setPassword(boardPassword.getText());
        }
        final Board addedBoard = server.addBoard(newBoard);
        Logger.log("Added board " + addedBoard);

        final CardList cardList = new CardList("TODO");
        cardList.setBoard(addedBoard);
        final CardList addedCardList = server.addCardList(cardList);
        addedBoard.addCardList(addedCardList);

        clear();
        BoardCtrl boardCtrl = factory.create(BoardCtrl.class, addedBoard);
        client.setBoardCtrl(boardCtrl);
        clientPrefs.setDefaultBoardId(addedBoard.getId());
        clientPrefs.addJoinedBoard(addedBoard.getId());
        mainCtrl.showMainView();
    }

    public void goBack() {
        clear();
        mainCtrl.showMainView();
    }

    public void pickColor(ActionEvent action){
        color = boardColor.getValue();
    }

    public void clear() {
        boardName.setText("");
        boardPassword.setText("");
    }
}
