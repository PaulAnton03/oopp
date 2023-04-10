package client.scenes;

import client.utils.ClientPreferences;
import client.utils.Logger;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.concurrent.CompletableFuture;

public class ServerConnectCtrl implements SceneCtrl {

    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    private final ClientPreferences clientPreferences;
    @FXML
    private TextField serverInput;
    @FXML
    private TextField inviteKeyInput;

    @Inject
    public ServerConnectCtrl(ServerUtils server, MainCtrl mainCtrl, ClientPreferences clientPreferences) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = server;
        this.clientPreferences = clientPreferences;
    }

    public void connect() {
        if (checkInviteKey() || serverInput.getText().isEmpty()) {
            serverUtils.setAdmin(false);
            connectToServer();
        } else {
            checkCustomServer();
        }
    }

    private boolean checkInviteKey() {
        String inviteKey = inviteKeyInput.getText();
        if (inviteKey.isEmpty()) return false;

        // Parse the invite key
        // The key format is: "join-talio#<server address>#<board id>(#<password>)"
        if (!inviteKey.matches("^join-talio#.{5,30}#[0-9]+(?:#.+)?$")) {
            throw new RuntimeException("The invite key is invalid, maybe copy it again");
        }
        String[] parts = inviteKey.split("#");

        serverUtils.setServerPath(parts[1]);
        final long boardId = Long.parseLong(parts[2]);
        clientPreferences.setDefaultBoardId(boardId);
        if (parts.length > 3) {
            clientPreferences.setPasswordForBoard(boardId, parts[3]);
        }

        // Check if the board id references real board
        try {
            Board board = serverUtils.getBoard(boardId);
            if (board.getPassword() != null &&
                    !board.getPassword().equals(clientPreferences.getPasswordForBoard(boardId).orElse(null))) {
                throw new Exception();
            }
            return true;
        } catch (Exception e) {
            throw new RuntimeException("The invite key is invalid, maybe copy it again");
        }
    }

    private void checkCustomServer() {
        String serverPath = serverInput.getText();

        boolean isRunningAdmin = serverPath.startsWith("admin//");
        serverUtils.setAdmin(isRunningAdmin);
        serverUtils.setServerPath(isRunningAdmin ? serverPath.substring(7) : serverPath);

        if (isRunningAdmin) {
            checkAdminPassword();
        } else {
            connectToServer();
        }
    }

    private void checkAdminPassword() {
        CompletableFuture<Boolean> correctPassword = mainCtrl.getAdminPasswordCtrl().loadFuture();
        mainCtrl.showAdminPasswordProtected();
        correctPassword.thenAccept((correct) -> {
            if (correct) {
                //clientPreferences.clearPreferences();
                connectToServer();
            }
        });
    }

    private void connectToServer() {
        // Connect to server
        try {
            serverUtils.connect();
        } catch (Exception e) {
            throw new RuntimeException("Server address invalid");
        }
        mainCtrl.showMainView();
        Logger.log("Connecting to server: " + serverUtils.getServerPath());
    }
}
