package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ServerConnectCtrl {

    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    @FXML
    private TextField serverInput;

    @Inject
    public ServerConnectCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = server;
    }

    public void connect() {
        String server = serverInput.getText();
        if (server == null || server.isEmpty()) {
            server = serverUtils.getServerPath();
        }

        // Connect to server
        serverUtils.setServerPath(server);
        System.out.println("Connecting to server: " + server);

        // Switching the scene
        mainCtrl.getPrimaryStage().setResizable(true);
        mainCtrl.showMainView();
    }
}
