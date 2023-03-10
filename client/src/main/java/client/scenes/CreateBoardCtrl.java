package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;

public class CreateBoardCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML private TextField boardName;
    @FXML private ColorPicker boardColor;
    @FXML private TextField boardPassword;
    @FXML private CheckBox passwordUsed;

    @Inject
    public CreateBoardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    public void createBoard() {
        // TODO create a new board
        System.out.println("Board name: " + boardName.getText());
        System.out.println("Board color: " + boardColor.getValue());
        System.out.println("Board password: " + boardPassword.getText());
        System.out.println("Password used: " + passwordUsed.isSelected());
    }

    public void goBack() { mainCtrl.showMainView(); }
}
