package client.scenes;

import client.components.BoardCtrl;
import client.components.CardCtrl;
import client.components.CardListCtrl;
import client.components.TagCtrl;
import client.utils.*;
import com.google.inject.Inject;
import commons.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import org.springframework.messaging.simp.stomp.StompSession;

import java.util.ArrayList;
import java.util.List;


public class MainViewCtrl implements SceneCtrl {

    private final ServerUtils server;
    private final ClientUtils client;
    private final MainCtrl mainCtrl;
    private final ComponentFactory factory;

    private final ExceptionHandler exceptionHandler;

    @FXML
    private ScrollPane boardContainer;
    @FXML
    private Text displayBoardName;
    @FXML
    private BorderPane root;

    @FXML
    private Label adminLabel;

    @FXML
    private AnchorPane warning;


    private final List<StompSession.Subscription> subscriptions = new ArrayList<>();

    private boolean register = true;

    @Inject
    public MainViewCtrl(ServerUtils server, ClientUtils client, MainCtrl mainCtrl,
                        ComponentFactory factory, ExceptionHandler exceptionHandler) {
        this.server = server;
        this.client = client;
        this.mainCtrl = mainCtrl;
        this.factory = factory;
        this.exceptionHandler = exceptionHandler;
    }

    @FXML
    void btnAddClicked(ActionEvent event) {
        if (!client.getBoardCtrl().getBoard().isEditable()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("You do not have permissions to edit this board.");
            alert.showAndWait();
        } else {
            mainCtrl.showAddList();
        }
    }

    @FXML
    void btnBackClicked(ActionEvent event) {
        unsubscribe();
        server.disconnect();
        mainCtrl.showConnect();
    }

    @FXML
    void btnCreateClicked(ActionEvent event) {
        mainCtrl.showCreate();
    }

    @FXML
    void btnJoinClicked(ActionEvent event) {
        mainCtrl.showJoin();
    }

    @FXML
    void btnSettingsClicked(ActionEvent event) {
        if (!client.getBoardCtrl().getBoard().isEditable()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("You do not have permissions to edit this board.");
            alert.showAndWait();
        } else {
            mainCtrl.showSettings();
        }
    }

    public void helpButtonClicked() {
        KeyEventHandler.displayKeybinds();
    }

    public void scrollHandler(ScrollEvent event) {
        double scrollAmount = 0.3;
        ScrollPane pane = (ScrollPane) event.getSource();

        if (event.getDeltaY() > 0 && pane.getHvalue() != pane.getHmax()) {
            pane.setHvalue(pane.getHvalue() + scrollAmount);
        }
        if (event.getDeltaY() < 0 && pane.getHvalue() != pane.getHmin()) {
            pane.setHvalue(pane.getHvalue() - scrollAmount);
        }
        event.consume();
    }

    public void loadData(Board board) {
        boolean admin = server.isAdmin();
        if (!admin) {
            adminLabel.setVisible(false);
        }

        BoardCtrl boardCtrl = factory.create(BoardCtrl.class, board);
        boardContainer.setContent(boardCtrl.getNode());
        boardContainer.setStyle("-fx-background: " + board.getBoardColor());
        displayBoardName.setText(board.getName());
        warning.setVisible(!board.isEditable());
        if (register)
            registerForMessages();
    }
    /**
     * This method is used after loading the data in order to subscribe the client to different endpoints
     * and handle events related to deleting, creating, and updating cards and lists on the current board the
     * user is viewing.
     */
    public void registerForMessages() {
        register = false;
        long boardId = client.getBoardCtrl().getBoard().getId();
        subscriptions.add(server.registerForMessages("/topic/board/" + boardId + "/subtasks", SubTask.class, s -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    client.getCardCtrl(client.getCard(s.getCard().getId()).getId()).refresh();
                    mainCtrl.getActiveCtrl().revalidate();
                    client.postRefresh();
                }});
        }));
        tagRegistration(boardId);
        /**
         * This method call handles the deletion,addition and updating of a card on the current board by
         * using the registerForMessages method of the server, which refreshes the CardListCtrl of the card
         * in question.
         */
        subscriptions.add(server.registerForMessages("/topic/board/" + boardId + "/cards", Card.class, c -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    client.getCardListCtrl(c.getCardList().getId()).refresh();
                    mainCtrl.getActiveCtrl().revalidate();
                    client.postRefresh();
                }});
        }));
        /**
         * This method call handles the deletion,addition and updating of a list on the current board by
         * using the registerForMessages method of the server, which refreshes the BoardCtrl of the list
         * in question.
         */
        subscriptions.add(server.registerForMessages("/topic/board/" + boardId + "/lists", CardList.class, l -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    client.getBoardCtrl().refresh();
                    mainCtrl.getActiveCtrl().revalidate();
                    client.postRefresh();
                }});
        }));
        subscriptions.add(server.registerForMessages("/topic/board/" + boardId + "/update", Board.class, b -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (mainCtrl.getActiveCtrl() == mainCtrl.getMainViewCtrl()) {
                        mainCtrl.showMainView(b);
                    } else {
                        client.getBoardCtrl().refresh();
                        mainCtrl.getActiveCtrl().revalidate();
                    }
                    client.postRefresh();
                }});
        }));
        /**
         * This method call is used for informing the client that the board they are currently on
         * has been deleted
         */
        subscriptions.add(server.registerForMessages("/topic/board/" + boardId + "/delete", Board.class, b -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    client.setBoardCtrl(null);
                    mainCtrl.getEditCardCtrl().resetState();
                    unsubscribe();
                    if (!mainCtrl.getActiveCtrl().equals(mainCtrl.getJoinBoardsCtrl())) {
                        mainCtrl.showJoin();
                        throw new RuntimeException("Sorry, but the board you are currently viewing has been permanently deleted.");
                    }
                }});}));
    }

    public void unsubscribe() {
        subscriptions.forEach(StompSession.Subscription::unsubscribe);
        subscriptions.clear();
        register = true;
    }

    @Override
    public void revalidate() {

    }
    private void tagRegistration(long boardId) {
        subscriptions.add(server.registerForMessages("/topic/board/" + boardId + "/cardtags",
                CardTag.class, c -> Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                client.getCardCtrl(c.getCard().getId()).refresh();
                                client.postRefresh();

                            } catch (Exception e) {
                                System.out.println("Some exception in websockets of CardTags!");
                            }
                        }
                    })));
        subscriptions.add(server.registerForMessages("/topic/board/" + boardId + "/cardtags/create",
                CardTag.class, c -> Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            client.getBoardCtrl().refresh();
                            for (CardTag cardTag : server.getCardTags()) {
                                CardCtrl cardCtrl = client.getCardCtrl(c.getCard().getId());
                                cardCtrl.refresh();
//                                mainCtrl.getActiveCtrl().revalidate();
                                client.postRefresh();
                            }
                        } catch (Exception e) {
                            System.out.println("Some exception in websockets of CardTags Creation!");
                        }
                    }
                })));
        subscriptions.add(server.registerForMessages("/topic/board/" + boardId + "/tags",
                Tag.class, c -> {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                client.getBoardCtrl().refresh();
                                for (CardTag cardTag : server.getCardTags()) {
                                    if (cardTag.getTag().equals(c)) {
                                        TagCtrl tagCtrl = client.getTagCtrl(c.getId());
                                        tagCtrl.refresh(cardTag.getCard());
//                                        mainCtrl.getActiveCtrl().revalidate();
                                    }
                                }
                            } catch (Exception ignored) {}
                        }
                    });
                }));
        subscriptions.add(server.registerForMessages("/topic/board/" + boardId + "/tags/delete",
                Tag.class, c -> {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Board board = client.getBoardCtrl().getBoard();
                                client.getBoardCtrl().refresh();
                                if(board != null){
                                    for(CardList cardList : board.getCardLists()){
                                        CardListCtrl cardListCtrl = client.getCardListCtrl(cardList.getId());
                                        cardListCtrl.refresh();
                                    }
                                }else{
                                    System.out.println("Board is null, removed tag could not be refreshed from" +
                                            " other clients!");
                                }
                            } catch (Exception e) {
                                System.out.println("Some exception in websockets of tags!");
                            }

                        }
                    });
                }));
    }
}
