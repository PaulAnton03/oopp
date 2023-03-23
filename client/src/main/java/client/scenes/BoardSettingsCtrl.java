package client.scenes;

import client.utils.ClientUtils;
import client.utils.ExceptionHandler;
import client.utils.Logger;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;

public class BoardSettingsCtrl {

    private final ServerUtils server;
    private final ClientUtils client;
    private final MainCtrl mainCtrl;

    private final ExceptionHandler exceptionHandler;

    @FXML
    private ColorPicker boardColor;
    @FXML
    private TextField boardPassword;
    @FXML
    private CheckBox passwordUsed;

    @Inject
    public BoardSettingsCtrl(ServerUtils server, ClientUtils client, MainCtrl mainCtrl, ExceptionHandler exceptionHandler) {
        this.client = client;
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.exceptionHandler = exceptionHandler;
    }

    public void saveChanges() {
        Board board = client.getActiveBoard();
        if (board == null) {
            exceptionHandler.clientException("Something went wrong, no board selected!");
            Logger.log("No board selected", Logger.LogLevel.ERROR);
            return;
        }
        board.setPassword(passwordUsed.isSelected() ? boardPassword.getText() : null);
        server.addBoard(board);
        mainCtrl.showMainView();

    }

    public void deleteBoard() {
        Board board = client.getActiveBoard();
        if (board == null) {
            exceptionHandler.clientException("Something went wrong, no board selected!");
            Logger.log("No board selected", Logger.LogLevel.ERROR);
            return;
        }
        server.deleteBoard(board.getId());
        Logger.log("Deleted board " + board);

        mainCtrl.showJoin();
    }

    public void goBack() {
        boardPassword.setText("");
        // TODO clear the form to current settings
        mainCtrl.showMainView();
    }

}
