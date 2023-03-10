package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

public class ServerConnectCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public ServerConnectCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

    public void connect() {

        //Connect to server

        mainCtrl.getPrimaryStage().setResizable(true);
        mainCtrl.showMainView();
        //Switch scene

    }

    public void showMainView() {
        mainCtrl.getPrimaryStage().setResizable(true);
        mainCtrl.showMainView();
    }
}
