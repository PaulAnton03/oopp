package client.utils;

import jakarta.ws.rs.BadRequestException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.net.ConnectException;

public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    public void uncaughtException(Thread t, Throwable e) {
        while (e.getCause() != null)
            e = e.getCause();

        Alert errorAlert = new Alert(AlertType.ERROR);

        String msg = "";
        if (e instanceof BadRequestException) {
            msg = "Bad request sent to server";
        } else if (e instanceof ConnectException) {
            msg = "Failed to connect to server";
        } else {
            msg = e.getMessage();
        }

        errorAlert.setHeaderText(msg);

        errorAlert.setContentText(e.getClass().getSimpleName());
        errorAlert.showAndWait();
    }

    public void clientException(String msg) {
        Alert errorAlert = new Alert(AlertType.ERROR);
        errorAlert.setHeaderText(msg);
        errorAlert.showAndWait();
    }
}
