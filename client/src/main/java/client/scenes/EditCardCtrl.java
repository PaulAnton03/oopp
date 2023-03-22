package client.scenes;

import client.utils.ClientUtils;
import client.utils.ServerUtils;
import commons.Card;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class EditCardCtrl {

    private final ServerUtils server;

    private final ClientUtils client;

    private final MainCtrl mainCtrl;

    @FXML
    private TextField changeTitle;

    @FXML
    private TextField changeDesc;

    @FXML
    private Button saveButton;

    public EditCardCtrl(ServerUtils server, ClientUtils client, MainCtrl mainCtrl) {
        this.server = server;
        this.client = client;
        this.mainCtrl = mainCtrl;
    }


}
