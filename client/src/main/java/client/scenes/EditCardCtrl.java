package client.scenes;

import client.components.TagCtrl;
import client.utils.ClientUtils;
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

import javax.inject.Inject;


public class EditCardCtrl implements SceneCtrl {
    private final ServerUtils server;
    private final ClientUtils client;
    private final MainCtrl mainCtrl;

    private ComponentFactory componentFactory;

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
                        ExceptionHandler exceptionHandler, ComponentFactory componentFactory) {
        this.server = server;
        this.client = client;
        this.mainCtrl = mainCtrl;
        this.exceptionHandler = exceptionHandler;
        this.componentFactory = componentFactory;
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
        Board curBoard = client.getBoardCtrl().getBoard();
        for(int a = 0; a < curBoard.getBoardTagId().length; a++ ){
            System.out.println("now on loadData editctrl: " + a);
            System.out.println(curBoard.getBoardTagText()[a]);
            System.out.println(curBoard.getBoardTagId()[a]);
            if(curBoard.getBoardTagId()[a] > 0) {
                Tag tag = new Tag(curBoard.getBoardTagText()[a],
                        curBoard.getBoardTagId()[a], curBoard.getBoardTagColor()[a]);
                TagCtrl tagCtrl = null;
                tagCtrl.loadData(tag);
                tagArea.getChildren().add(tagCtrl.getNode());
            }
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
        int id = board.getIdGenerator();
        board.getBoardTagText()[id] = tagText;
        board.getBoardTagId()[id] = id;
        board.getBoardTagColor()[id] = tagText;
        server.updateBoard(board);
        System.out.println("Tag created: " + tag);
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
