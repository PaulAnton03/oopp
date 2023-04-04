package client.scenes;

import javax.inject.Inject;

import client.utils.ClientUtils;
import client.utils.ExceptionHandler;
import client.utils.ServerUtils;
import commons.Card;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javax.inject.Inject;


public class EditCardCtrl implements SceneCtrl {
    private final ServerUtils server;
    private final ClientUtils client;
    private final MainCtrl mainCtrl;

    private final ExceptionHandler exceptionHandler;
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


    @Inject
    public EditCardCtrl(ServerUtils server, ClientUtils client, MainCtrl mainCtrl, ExceptionHandler exceptionHandler) {
        this.server = server;
        this.client = client;
        this.mainCtrl = mainCtrl;
        this.exceptionHandler = exceptionHandler;
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
        if(colourPicker.getValue() != null)
            tag.setColor(colourPicker.getValue().toString());
        client.getCard(cardId).getTags().add(tag);
        client.getBoardCtrl().getBoard().getTagList().add(tag);
        tag.getCards().add(client.getCard(cardId));
        tag.setBoard(client.getBoardCtrl().getBoard());
        server.createTag(tag);
        server.updateCard(client.getCard(cardId));
        server.updateBoard(client.getBoardCtrl().getBoard());
        System.out.println(client.getCard(cardId));
        System.out.println(client.getBoardCtrl().getBoard());
    }

    public void assignTag(){

    }

    @Override
    public void revalidate() {
        if (client.getCardCtrls().containsKey(cardId)) {
            return;
        }
        mainCtrl.showMainView();
        throw new RuntimeException("Sorry, but the card you were editing or the list it was part of has been permanently deleted.");
    }
}
