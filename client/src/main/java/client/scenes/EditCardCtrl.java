package client.scenes;

import client.components.TagCtrl;
import client.utils.ClientUtils;
import client.utils.ComponentFactory;
import client.utils.ExceptionHandler;
import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
import commons.CardTag;
import commons.Tag;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import lombok.Getter;

import javax.inject.Inject;


public class EditCardCtrl implements SceneCtrl {
    private final ServerUtils server;
    private final ClientUtils client;
    private final MainCtrl mainCtrl;

    private ComponentFactory factory;

    private final ExceptionHandler exceptionHandler;

    @Getter
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
    private FlowPane tagArea;

    private Color color;

    @Inject
    public EditCardCtrl(ServerUtils server, ClientUtils client, MainCtrl mainCtrl,
                        ExceptionHandler exceptionHandler, ComponentFactory componentFactory) {
        this.server = server;
        this.client = client;
        this.mainCtrl = mainCtrl;
        this.exceptionHandler = exceptionHandler;
        this.factory = componentFactory;
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
        resetState();
        this.cardId = cardId;
        Card card = client.getCard(cardId); // was from server
        changeTitle.setText(card.getTitle());
        changeDesc.setText(card.getDescription());
        Board curBoard = client.getBoardCtrl().getBoard();
        for (Tag tag : curBoard.getTagList()) {
            TagCtrl tagCtrl = factory.create(TagCtrl.class, tag);
            for (CardTag cardTag : server.getCardTags()) {
                if (cardTag.getTag().equals(tag) && cardTag.getCard().equals(card)) {
                    tagCtrl.setAssigned(true);
                    break;
                }
            }
            tagCtrl.loadData(tag);
            tagArea.getChildren().add(tagCtrl.getNode());
        }
    }

    public void cancel() {
        resetState();
        mainCtrl.showMainView();
    }

    @FXML
    public void createTag() {
        Card card = client.getCard(cardId);
        System.out.println(card);
        Board board = card.getCardList().getBoard();
        System.out.println(card.getCardList());
        System.out.println(board);
        String tagText = "";
        String tagColor = "";
        if (tagField == null) {
            tagText = "";
        } else {
            tagText = tagField.getText();
        }
        if (color == null) {
            tagColor = "FFFFFF";
        } else {
            tagColor = color.toString();
        }
        Tag tag = new Tag();
        tag.setText(tagText);
        tag.setBoard(board); //because adding it to the board will save it
        tag.setColor(tagColor);
        server.createTag(tag, board.getId()); //this includes adding to board

        TagCtrl tagCtrl = factory.create(TagCtrl.class, tag);
        tagCtrl.loadData(tag);
        tagArea.getChildren().add(tagCtrl.getNode());
        System.out.println("Tag created: " + tag);
    }

    @FXML
    public void onKeyPressed(KeyEvent e) {
        if (KeyCode.ESCAPE == e.getCode()) {
            cancel();
        }
    }

    public void pickColor(ActionEvent event) {
        color = colourPicker.getValue();
    }


    public void resetState() {
        this.changeTitle.setText("");
        this.changeDesc.setText("");
        this.tagField.setText("");
        tagArea.getChildren().clear();
    }

    @Override
    public void revalidate() {
        if (client.getCardCtrls().containsKey(cardId)) {
            return;
        }
        mainCtrl.showMainView();
        throw new RuntimeException("Sorry, but the card you were " +
                "editing or the list it was part of has been permanently deleted.");
    }
}
