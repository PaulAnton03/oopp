package client.scenes;

import javax.inject.Inject;

import client.components.TagCtrl;
import client.utils.ClientUtils;
import client.utils.ComponentFactory;
import client.utils.ExceptionHandler;
import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;


public class EditCardCtrl implements SceneCtrl {
    private final ServerUtils server;
    private final ClientUtils client;
    private final MainCtrl mainCtrl;

    private final ComponentFactory factory;

    private final ExceptionHandler exceptionHandler;
    private long cardId;

    @FXML
    private ColorPicker colourPicker;

    @FXML
    private Button addTagButton;

    @FXML
    private TextField tagField;
    @FXML
    private TextField changeTitle;
    @FXML
    private TextArea changeDesc;

    @FXML
    private Pane tagArea;

    @Inject
    public EditCardCtrl(ServerUtils server, ClientUtils client, MainCtrl mainCtrl,
                        ExceptionHandler exceptionHandler, ComponentFactory factory) {
        this.server = server;
        this.client = client;
        this.mainCtrl = mainCtrl;
        this.factory = factory;
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
        for(Tag tag : client.getBoardCtrl().getBoard().getBoardTagList()){
            TagCtrl tagCtrl =
            tagArea.getChildren().add();
            //todo finish this with factory and such
        }

    }

    public void cancel() {
        resetState();
        mainCtrl.showMainView();
    }

    public void createTag() {
        Board board = client.getBoardCtrl().getBoard();
        board.setIdGenerator(board.getIdGenerator() + 1);
        String tagText = "";
        String tagColor = "";
        if (tagField.getText() == null) {
            tagText = "";
        } else {
            tagText = tagField.getText();
        }
        if (colourPicker.getValue() == null) {
            tagColor = "FFFFFF";
        } else {
            tagColor = colourPicker.getValue().toString();
        }
        Tag tag = new Tag(tagText, board.getIdGenerator(), tagColor);
        board.getBoardTagList().add(tag);
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

    @Override
    public void revalidate() {
        if (client.getCardCtrls().containsKey(cardId)) {
            return;
        }
        mainCtrl.showMainView();
        throw new RuntimeException("Sorry, but the card you were editing or the list it was part of has been permanently deleted.");
    }
}
