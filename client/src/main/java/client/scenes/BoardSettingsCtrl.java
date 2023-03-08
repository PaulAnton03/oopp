package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;

public class BoardSettingsCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public BoardSettingsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

}
