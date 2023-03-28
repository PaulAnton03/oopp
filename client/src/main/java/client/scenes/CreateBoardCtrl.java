package client.scenes;

import client.utils.ClientUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;

public class CreateBoardCtrl {

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

    @Inject
    public CreateBoardCtrl(ServerUtils server, ClientUtils client, MainCtrl mainCtrl) {
        this.client = client;
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void createBoard() {
        // TODO: Implement board colors.

        final Board newBoard = new Board(boardName.getText());
        if (passwordUsed.isSelected()) {
            newBoard.setPassword(boardPassword.getText());
        }
        final Board addedBoard = server.addBoard(newBoard);
        System.out.println("Added board " + addedBoard);

        final CardList cardList = new CardList("TODO");
        cardList.setBoard(addedBoard);
        final CardList addedCardList = server.addCardList(cardList);
        addedBoard.addCardList(addedCardList);

        clear();
        server.send("/app/boards", addedBoard);
        mainCtrl.showMainView(addedBoard);
    }

    public void goBack() {
        clear();
        mainCtrl.showMainView();
    }

    public void clear() {
        boardName.setText("");
        boardPassword.setText("");
    }
}
