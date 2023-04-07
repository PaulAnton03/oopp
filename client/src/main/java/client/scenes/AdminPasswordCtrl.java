package client.scenes;

import client.utils.ExceptionHandler;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

public class AdminPasswordCtrl implements SceneCtrl {
    private MainCtrl mainCtrl;
    private ExceptionHandler exceptionHandler;
    @FXML
    private PasswordField passwordField;
    private CompletableFuture<Boolean> correctPassword;

    @Inject
    public AdminPasswordCtrl(MainCtrl mainCtrl, ExceptionHandler exceptionHandler) {
        this.mainCtrl = mainCtrl;
        this.exceptionHandler = exceptionHandler;
    }

    public CompletableFuture<Boolean> loadFuture() {
        this.correctPassword = new CompletableFuture<>();
        return this.correctPassword;
    }

    public void buttonCancelClicked() {
        passwordField.clear();
        this.mainCtrl.showConnect();
        this.correctPassword.complete(false);
    }

    public void checkPassword() {
        String inputtedPassword = passwordField.getText();
        passwordField.clear();

        if (inputtedPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty!");
        }

        if (!inputtedPassword.equals("admin")) {
            throw new IllegalArgumentException("Incorrect Password!");
        }

        correctPassword.complete(true);
    }
}
