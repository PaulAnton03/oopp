package client.scenes;

import client.utils.ExceptionHandler;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.Setter;

import javax.inject.Inject;

public class PasswordProtectedCtrl implements SceneCtrl {

    private MainCtrl mainCtrl;
    private ExceptionHandler exceptionHandler;
    @FXML
    private PasswordField passwordField;

    @Setter
    private Board passwordBoard;

    @Inject
    public PasswordProtectedCtrl(MainCtrl mainCtrl, ExceptionHandler exceptionHandler) {
        this.mainCtrl = mainCtrl;
        this.exceptionHandler = exceptionHandler;
    }

    public void loadData(Board passwordBoard) {
        this.passwordBoard = passwordBoard;
    }

    public void buttonCancelClicked() {
        passwordField.clear();
        this.mainCtrl.showJoin();
    }

    public void checkPassword() {
        final String inputtedPassword = passwordField.getText();
        passwordField.clear();

        if (inputtedPassword.isEmpty()) {
            exceptionHandler.clientException("Password cannot be empty!");
            return;
        }

        if (!inputtedPassword.equals(passwordBoard.getPassword())) {
            exceptionHandler.clientException("Incorrect Password!");
            return;
        }
        passwordBoard.setEditable(true);
        mainCtrl.showMainView(passwordBoard);
    }

    public void readOnly() {
        passwordBoard.setEditable(false);
        mainCtrl.showMainView(passwordBoard);
    }
}
