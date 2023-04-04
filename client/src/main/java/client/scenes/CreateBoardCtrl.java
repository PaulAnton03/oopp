package client.scenes;

import client.utils.ClientUtils;
import client.utils.Logger;
import client.utils.ServerUtils;
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
    public CreateBoardCtrl(ServerUtils server, ClientUtils client, MainCtrl mainCtrl) {
        this.client = client;
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void createBoard() {

        final Board newBoard = new Board(boardName.getText());
        if(color != null)
            newBoard.setBoardColor(color.toString());
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
        mainCtrl.showMainView(addedBoard);
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
