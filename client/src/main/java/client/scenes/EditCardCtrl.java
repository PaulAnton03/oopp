package client.scenes;

import javax.inject.Inject;

import client.utils.ClientUtils;
import client.utils.ServerUtils;
import commons.Card;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;


public class EditCardCtrl {
    private final ServerUtils server;
    private final ClientUtils client;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField changeTitle;
    @FXML
    private TextArea changeDesc;

    @FXML
    private Button addTagButton;

    @FXML
    private TextField tagField;
    @FXML
    private ColorPicker colourPicker;

    @FXML
    private ComboBox comboBox;

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


    public void createTag(){
        Tag tag = new Tag();
        tag.setText(tagField.getText());
        tag.setColor(new java.awt.Color(
                (float) colourPicker.getValue().getRed(),
                (float) colourPicker.getValue().getBlue(),
                (float) colourPicker.getValue().getGreen()));
        client.getActiveCard().getTagList().add(tag);
        server.addTag(tag);
        new Alert(Alert.AlertType.INFORMATION, "New Tag Added!");
    }
}
