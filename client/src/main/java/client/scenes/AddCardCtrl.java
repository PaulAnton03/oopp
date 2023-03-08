package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class AddCardCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML private TextField title;
    @FXML private TextArea description;

    @Inject
    public AddCardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    public void handleAdd() {
        System.out.println("Title: " + title.getText());
        System.out.println("Description: " + description.getText());
    }

    public void handleClear() {
        title.setText("Title");
        description.setText("Description");
    }

    public void goBack() {
        handleClear();
        mainCtrl.showMainView();
    }
}
