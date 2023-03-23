package client.scenes;

import client.components.CardCtrl;
import client.utils.ClientUtils;
import client.utils.ServerUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.Setter;

import javax.inject.Inject;


public class EditCardCtrl {

    private final ServerUtils server;

    private final ClientUtils client;

    private final MainCtrl mainCtrl;

    @FXML
    private TextField changeTitle;
    @FXML
    private TextArea changeDesc;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private CheckBox checkBoxImp;
    @FXML
    private CheckBox checkBoxChr;
    @FXML
    private CheckBox checkBoxPro;
    @FXML
    private CheckBox checkBoxOpt;

    @Setter
    private CardCtrl cardCtrl;

    @Inject
    public EditCardCtrl(ServerUtils server, ClientUtils client, MainCtrl mainCtrl) {
        this.server = server;
        this.client = client;
        this.mainCtrl = mainCtrl;
        this.cardCtrl = null;
    }


    public void saveCardChanges(){
        //todo update the card instead of deleting and adding again
        server.deleteCard(cardCtrl.getCard().getId());
        cardCtrl.getCard().setTitle(changeTitle.getText());
        cardCtrl.getCard().setDescription(changeDesc.getText());
        server.addCard(cardCtrl.getCard());
        cardCtrl.loadData(cardCtrl.getCard());
        mainCtrl.showMainView();
    }

    public void cancel() {
        changeTitle.setText("");
        changeDesc.setText("");
        mainCtrl.showMainView();
    }

}
