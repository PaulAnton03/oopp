package client.scenes;

import client.utils.ServerUtils;
import commons.Card;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

import javax.inject.Inject;

public class AddCardCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField title;
    @FXML
    private TextArea description;

    @Inject
    public AddCardCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    private Card getCard() {
        return new Card(title.getText(), description.getText());
    }

    public void ok() {
        try {
            server.addCard(getCard());
        } catch (WebApplicationException e) {

            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        clearForm();
        mainCtrl.showMainView(server.getSelectedBoard());
    }

    public void clearForm() {
        title.setText("");
        description.setText("");
    }

    public void goBack() {
        clearForm();
        mainCtrl.showMainView(server.getSelectedBoard());
    }
}
