package client.scenes;

import javax.inject.Inject;

import client.components.SubTaskCtrl;
import client.utils.ClientUtils;
import client.utils.ComponentFactory;
import client.utils.ExceptionHandler;
import client.utils.ServerUtils;
import commons.Card;
import commons.SubTask;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.Getter;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.ArrayList;
import java.util.List;


public class EditCardCtrl implements SceneCtrl {
    private final ServerUtils server;
    private final ClientUtils client;
    private final MainCtrl mainCtrl;

    private final ComponentFactory factory;

    private final ExceptionHandler exceptionHandler;
    private long cardId;

    @FXML
    private TextField changeTitle;
    @FXML
    private TextArea changeDesc;

    @Getter
    @FXML
    private VBox subTaskView;

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
        if(changeTitle.getText().isEmpty()) {
            throw new IllegalStateException("Card must have a title");
        }
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
        for (SubTask subTask : card.getSubtasks()) {
            SubTaskCtrl subTaskCtrl = factory.create(SubTaskCtrl.class, subTask);
            subTaskView.getChildren().add(subTaskCtrl.getNode());
        }
        registerForMessages();
    }

    public void registerForMessages() {
        long boardId = client.getCard(cardId).getCardList().getBoard().getId();

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

    public void resetState() {
        this.changeTitle.setText("");
        this.changeDesc.setText("");
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
