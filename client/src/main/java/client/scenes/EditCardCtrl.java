package client.scenes;

import javax.inject.Inject;

import client.utils.ClientUtils;
import client.utils.ServerUtils;
import commons.Card;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;

import java.awt.*;
import java.awt.event.ActionEvent;


public class EditCardCtrl {
    private final ServerUtils server;
    private final ClientUtils client;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField changeTitle;
    @FXML
    private TextField changeDesc;

    @FXML
    private Button addTagButton;

    @FXML
    private TextField tagField;

    @FXML
    private ColorPicker colourPicker;

    @Inject
    public EditCardCtrl(ServerUtils server, ClientUtils client, MainCtrl mainCtrl) {
        this.server = server;
        this.client = client;
        this.mainCtrl = mainCtrl;
    }

    public void saveCardChanges(){
        Card card = client.getActiveCardCtrl().getCard();
        card.setTitle(changeTitle.getText());
        card.setDescription(changeDesc.getText());
        server.updateCard(card);
        client.getActiveCardCtrl().loadData(card);
        resetState();
        mainCtrl.showMainView();
    }

    public void cancel() {
        resetState();
        mainCtrl.showMainView();
    }

    @FXML
    public void onKeyPressed(KeyEvent e) {
        if (KeyCode.ESCAPE == e.getCode()) {
            cancel();
        }
    }

    public void resetState() {
        this.changeTitle.setText("");
        this.changeDesc.setText("");
    }

    public void changeColor(ActionEvent event) {
        Color myColor = colourPicker.getValue();
    }
}
