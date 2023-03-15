package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;

public class CreateBoardCtrl {

    private final ServerUtils server;
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
    public CreateBoardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void createBoard() {
        // TODO: Implement board colors.

        Board board = new Board(boardName.getText());
        if (passwordUsed.isSelected()) {
            board.setPassword(boardPassword.getText());
        }

        board = server.addBoard(board);
        mainCtrl.showMainView(board);

        server.setSelectedBoard(board);
        System.out.println("Saved board " + board);
    }

    public void goBack() {
        boardName.setText("");
        boardPassword.setText("");
        mainCtrl.showMainView(server.getSelectedBoard());
    }
}
