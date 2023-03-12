package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ServerConnectCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    @FXML
    private TextField serverInput;

    @Inject
    public ServerConnectCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

    public void connect() {
        String server = serverInput.getText();
        if (server == null || server.isEmpty()) {
            server = ServerUtils.SERVER;
        }
        //Connect to server
        ServerUtils.SERVER = server;
        System.out.println("Connecting to server: " + server);

        mainCtrl.getPrimaryStage().setResizable(true);
        mainCtrl.showMainView();
        //Switch scene

    }
}
