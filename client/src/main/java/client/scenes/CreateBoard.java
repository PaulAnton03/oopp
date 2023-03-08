package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CreateBoard {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Inject
    public CreateBoard(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;

    }

    @FXML
    private TextField boardName;

    @FXML
    void buttonCancel(ActionEvent event) {
        System.out.println("You clicked cancel");
    }

    @FXML
    void buttonCreate(ActionEvent event) {
        System.out.println("You entered: " + boardName.getText());
        boardName.setText("");
    }

}
