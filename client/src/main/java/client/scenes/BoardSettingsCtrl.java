package client.scenes;

import com.google.inject.Inject;

import client.utils.ClientUtils;
import client.utils.ExceptionHandler;
import client.utils.Logger;
import client.utils.ServerUtils;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;

public class BoardSettingsCtrl {

    private final ServerUtils server;
    private final ClientUtils client;
    private final MainCtrl mainCtrl;

    @FXML
    private ColorPicker boardColor;
    @FXML
    private TextField boardPassword;
    @FXML
    private CheckBox passwordUsed;

    @FXML
    private Button deleteBoardButton;

    @Inject
    public BoardSettingsCtrl(ServerUtils server, ClientUtils client, MainCtrl mainCtrl, ExceptionHandler exceptionHandler) {
        this.client = client;
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void load() {
        if (!server.isAdmin()) {
            deleteBoardButton.setDisable(true);
            deleteBoardButton.setVisible(false);
            return;
        }
        deleteBoardButton.setDisable(false);
        deleteBoardButton.setVisible(true);
    }

    public void saveChanges() {
        Board board = client.getBoardCtrl().getBoard();
        if (board == null) {
            Logger.log("No board selected", Logger.LogLevel.ERROR);
            throw new IllegalStateException("Something went wrong, no board selected!");
        }
        board.setPassword(passwordUsed.isSelected() ? boardPassword.getText() : null);
        server.updateBoard(board);
        client.getBoardCtrl().refresh();
        clearForm();
        mainCtrl.showMainView();
    }

    public void deleteBoard() {
        Board board = client.getBoardCtrl().getBoard();
        if (board == null) {
            Logger.log("No board selected", Logger.LogLevel.ERROR);
            throw new IllegalStateException("Something went wrong, no board selected!");
        }
        server.deleteBoard(board.getId());
        client.getBoardCtrl().remove();
        Logger.log("Deleted board " + board);
        mainCtrl.showJoin();
    }

    public void goBack() {
        clearForm();
        mainCtrl.showMainView();
    }

    public void clearForm() {
        boardPassword.setText("");
        passwordUsed.setSelected(false);
    }
}
