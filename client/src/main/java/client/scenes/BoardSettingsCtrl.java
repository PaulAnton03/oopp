package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;

public class BoardSettingsCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private ColorPicker boardColor;
    @FXML
    private TextField boardPassword;
    @FXML
    private CheckBox passwordUsed;

    @Inject
    public BoardSettingsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void saveChanges() {
        Board board = server.getSelectedBoard();
        if(board == null) {
            //TODO: Shouldn't reach this point without a board selected.
            System.out.println("No board selected");
            return;
        }
        board.setPassword(passwordUsed.isSelected() ? boardPassword.getText() : null);
        server.addBoard(board);
        mainCtrl.showMainView();

    }

    public void deleteBoard() {
        // TODO ask user for password (if there is one)
        System.out.println("Board deleted");
    }

    public void goBack() {
        boardPassword.setText("");
        // TODO clear the form to current settings
        mainCtrl.showMainView();
    }

}
