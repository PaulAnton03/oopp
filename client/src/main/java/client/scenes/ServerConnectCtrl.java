package client.scenes;

import client.utils.ClientPreferences;
import client.utils.Logger;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.concurrent.CompletableFuture;

public class ServerConnectCtrl {

    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    private final ClientPreferences clientPreferences;
    @FXML
    private TextField serverInput;

    @Inject
    public ServerConnectCtrl(ServerUtils server, MainCtrl mainCtrl,ClientPreferences clientPreferences) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = server;
        this.clientPreferences = clientPreferences;
    }

    public void connect() {
        String serverPath = serverInput.getText();

        if (!serverPath.isEmpty()) {
            if (!handleCustomServer(serverPath)) {
                return;
            }
        }

        connectToServer();
    }

    private boolean handleCustomServer(String serverPath) {
        boolean admin = serverPath.startsWith("admin//");
        if (admin) {
            serverPath = serverPath.substring(7);
        }
        serverUtils.setAdmin(admin);
        serverUtils.setServerPath(serverPath);

        if (admin) {
            askForAdminPassword();
            return false;
        }
        return true;
    }

    private void askForAdminPassword() {
        CompletableFuture<Boolean> correctPassword = mainCtrl.getAdminPasswordCtrl().loadFuture();
        mainCtrl.showAdminPasswordProtected();
        correctPassword.thenAccept((correct) -> {
            if (correct) {
                clientPreferences.clearPreferences();
                connectToServer();
            }
        });
    }

    private void connectToServer() {
        // Connect to server
        serverUtils.connect();
        mainCtrl.showMainView();
        Logger.log("Connecting to server: " + serverUtils.getServerPath());
    }
}
