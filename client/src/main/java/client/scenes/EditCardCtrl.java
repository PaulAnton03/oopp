package client.scenes;

import client.utils.ClientUtils;
import client.utils.ServerUtils;
import commons.Card;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javax.inject.Inject;


public class EditCardCtrl {
    private final ServerUtils server;
    private final ClientUtils client;
    private final MainCtrl mainCtrl;
    private long cardId;

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

    public void saveCardChanges() {
        Card card = client.getCard(cardId);
        card.setTitle(changeTitle.getText());
        card.setDescription(changeDesc.getText());
        server.updateCard(card);
        client.getCardCtrl(cardId).refresh();
        resetState();
        mainCtrl.showMainView();
    }

    public void loadData(long cardId) {
        this.cardId = cardId;
        Card card = client.getCard(cardId);
        changeTitle.setText(card.getTitle());
        changeDesc.setText(card.getDescription());
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

    public void editTag() {
        Card card = server.getCard(cardId);
        server.updateTag(card.getTags().iterator().next());
    }

    public void assignTagToCard(long tagId){
        Tag tag = client.getTag(tagId);
        Card card = client.getCard(cardId);
        card.getTags().add(tag);
        server.updateCard(card);

        tag.getCards().add(card);
        server.getCard(cardId).getTags().add(tag);
        server.getTag(tag.getId()).getCards().add(card);
        server.updateTag(tag);
    }

    public void resetState() {
        this.changeTitle.setText("");
        this.changeDesc.setText("");
        this.tagField.setText("");
    }


    public void createTag(){
        Tag tag = new Tag();
        tag.setText(tagField.getText());
        tag.setRed((int) colourPicker.getValue().getRed());
        tag.setBlue((int) colourPicker.getValue().getBlue());
        tag.setGreen((int) colourPicker.getValue().getGreen());
        client.getCard(cardId).getTags().add(tag);
        tag.getCards().add(client.getCard(cardId));
        server.createTag(tag);
        new Alert(Alert.AlertType.INFORMATION, "New Tag Added!");
    }

    public void assignTag(){

    }
}
