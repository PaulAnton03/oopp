package client.scenes;

import client.utils.ServerUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class JoinBoardsCtrl {

    private final ServerUtils server;

    private final MainCtrl mainCtrl;

    @FXML private TextField boardPasswordField;
    @FXML private Button btnClear;
    @FXML private Button btnOk;
    @FXML private Button navbarBackButton;
    @FXML private Button navbarCreateButton;

    @Inject
    public JoinBoardsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @FXML
    void addCardClicked(ActionEvent event) {
        System.out.println("Password set: " + boardPasswordField.getText());
    }

    @FXML
    void btnBackClicked(ActionEvent event) {
        boardPasswordField.setText("");
        mainCtrl.showMainView();
    }

    @FXML
    void btnClearClicked(ActionEvent event) {
        boardPasswordField.setText("");
        System.out.println("Cleared!");
    }

    @FXML
    void btnCreateClicked(ActionEvent event) {
        mainCtrl.showCreate();
    }

}
