package client.scenes;

import client.components.BoardCtrl;
import client.components.TagCtrl;
import javax.inject.Inject;
import client.components.SubTaskCtrl;
import client.utils.ClientUtils;
import client.utils.ComponentFactory;
import client.utils.ExceptionHandler;
import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
import commons.CardTag;
import commons.Tag;
import javafx.event.ActionEvent;
import commons.SubTask;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.Getter;
import javafx.scene.layout.VBox;
import lombok.Setter;
import org.springframework.messaging.simp.stomp.StompSession;
import java.util.ArrayList;
import java.util.List;


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

    @Getter
    @FXML
    private VBox subTaskView;

    @FXML
    private Text emptyErr;
    @Getter
    @Setter
    @FXML
    private Text tagAssignText;
    private final List<StompSession.Subscription> subscriptions = new ArrayList<>();


    @Inject
    public EditCardCtrl(ServerUtils server, ClientUtils client, MainCtrl mainCtrl, ComponentFactory factory, ExceptionHandler exceptionHandler) {
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
        resetState();
        this.cardId = cardId;
        Card card = client.getCard(cardId);
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
        for (SubTask subTask : card.getSubtasks()) {
            SubTaskCtrl subTaskCtrl = factory.create(SubTaskCtrl.class, subTask);
            subTaskView.getChildren().add(subTaskCtrl.getNode());
        }
        registerForMessages();
    }

    public void registerForMessages() {
        long boardId = client.getCard(cardId).getCardList().getBoard().getId();
        subscriptions.add(server.registerForMessages("/topic/board/" + boardId + "/tags/create", Tag.class, tag -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    TagCtrl tagCtrl = factory.create(TagCtrl.class, tag);
                    tagCtrl.loadData(tag);
                    tagArea.getChildren().add(tagCtrl.getNode());
                }
            });
        }));
        subscriptions.add(server.registerForMessages("/topic/board/" + boardId + "/tags/delete", Tag.class, tag -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    TagCtrl tagCtrl = factory.create(TagCtrl.class, tag);
                    tagCtrl.loadData(tag);
                    tagArea.getChildren().add(tagCtrl.getNode());
                }
            });
        }));


        /**
         * Create SubTask
         */
        subscriptions.add(server.registerForMessages("/topic/board/" + boardId + "/card/" + cardId + "/subtasks/create", SubTask.class, s -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    SubTaskCtrl subTaskCtrl = factory.create(SubTaskCtrl.class, s);
                    subTaskView.getChildren().add(subTaskCtrl.getNode());
                }
            });

        }));

        /**
         * Remove SubTask
         */
        subscriptions.add(server.registerForMessages("/topic/board/" + boardId + "/card/" + cardId + "/subtasks/delete", SubTask.class, s -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    subTaskView.getChildren().remove(client.getSubTaskCtrl(s.getId()).getNode());
                }
            });

        }));

        /**
         * Update SubTask
         */
        subscriptions.add(server.registerForMessages("/topic/board/" + boardId + "/card/" + cardId + "/subtasks/update", SubTask.class, s -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    client.getSubTaskCtrl(s.getId()).loadData(s);
                }
            });

        }));

        /**
         * Reorder SubTask up
         */
        subscriptions.add(server.registerForMessages("/topic/board/" + boardId + "/card/" + cardId + "/subtasks/reorder/up", SubTask.class, s -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    SubTaskCtrl ctrl = client.getSubTaskCtrl(s.getId());
                    int currentIndex = subTaskView.getChildren().indexOf(ctrl.getNode());
                    subTaskView.getChildren().remove(currentIndex);
                    subTaskView.getChildren().add(currentIndex - 1, ctrl.getNode());
                }
            });

        }));

        /**
         * Reorder SubTask down
         */
        subscriptions.add(server.registerForMessages("/topic/board/" + boardId + "/card/" + cardId + "/subtasks/reorder/down", SubTask.class, s -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    SubTaskCtrl ctrl = client.getSubTaskCtrl(s.getId());
                    int currentIndex = subTaskView.getChildren().indexOf(ctrl.getNode());
                    subTaskView.getChildren().remove(currentIndex);
                    subTaskView.getChildren().add(currentIndex + 1, ctrl.getNode());
                }
            });

        }));
    }

    public void addSubTask() {
        if (!client.getBoardCtrl().getBoard().isEditable()) {
            throw new IllegalStateException("You do not have permissions to edit this board.");
        }
        SubTask subTask = new SubTask("SubTask ", false);
        subTask.setCard(client.getCard(cardId));
        server.addSubTask(subTask);
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
        if (tagField == null || tagField.getText().equals("")) {
            emptyErr.setVisible(true);
            return;
        } else {
            emptyErr.setVisible(false);
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
        mainCtrl.showMainView();
        mainCtrl.showEditCard(cardId);
//        Tag newTag = server.getTag(tag.getId());
//        TagCtrl tagCtrl = factory.create(TagCtrl.class, newTag);
//        tagCtrl.loadData(newTag);
//        tagArea.getChildren().add(tagCtrl.getNode());
//        System.out.println("Tag created: " + newTag);
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
        tagAssignText.setVisible(false);
        emptyErr.setVisible(false);
        this.changeTitle.setText("");
        this.changeDesc.setText("");
        this.tagField.setText("");
        tagArea.getChildren().clear();
        subTaskView.getChildren().clear();
        subscriptions.forEach(StompSession.Subscription::unsubscribe);
        subscriptions.clear();
    }

    public void clearForm() {
        Card card = client.getCard(cardId);
        this.changeTitle.setText(card.getTitle());
        this.changeDesc.setText(card.getDescription());
    }

    @Override
    public void revalidate() {
        if (client.getCardCtrls().containsKey(cardId)) {
            return;
        }
        resetState();
        mainCtrl.showMainView();
        throw new RuntimeException("Sorry, but the card you were editing or the list it was part of has been permanently deleted.");
    }

}
