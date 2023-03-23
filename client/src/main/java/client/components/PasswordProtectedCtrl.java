package client.components;

import client.scenes.MainCtrl;
import client.utils.ExceptionHandler;
import commons.Board;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.Setter;

import javax.inject.Inject;

public class PasswordProtectedCtrl {

    private MainCtrl mainCtrl;
    private ExceptionHandler exceptionHandler;
    @FXML
    private TextField passwordField;

    @Setter
    private Board passwordBoard;

    @Inject
    public PasswordProtectedCtrl(MainCtrl mainCtrl, ExceptionHandler exceptionHandler) {
        this.mainCtrl = mainCtrl;
        this.exceptionHandler = exceptionHandler;
    }

    public void buttonCancelClicked(ActionEvent actionEvent) {
        this.mainCtrl.showJoin();
    }

    public void checkPassword(ActionEvent actionEvent) {
        if (!passwordField.getText().equals(passwordBoard.getPassword())) {
            exceptionHandler.clientException("Incorrect Password!");
            return;
        }
        mainCtrl.showMainView(passwordBoard);
    }
}
