package client.scenes;

import client.utils.*;
import com.google.inject.Inject;

import client.components.BoardCtrl;
import commons.Board;
import commons.Card;
import commons.CardList;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;


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

    @Inject
    public MainViewCtrl(ServerUtils server, ClientUtils client, MainCtrl mainCtrl, ComponentFactory factory, ExceptionHandler exceptionHandler) {
        this.server = server;
        this.client = client;
        this.mainCtrl = mainCtrl;
        this.factory = factory;
        this.exceptionHandler = exceptionHandler;
    }

    @FXML
    void btnAddClicked(ActionEvent event) {
        if(!client.getBoardCtrl().getBoard().isEditable()) {
            throw new IllegalStateException("You do not have permissions to edit this board.");
        }
        mainCtrl.showAddList();
    }

    @FXML
    void btnBackClicked(ActionEvent event) {
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
        if(!client.getBoardCtrl().getBoard().isEditable()) {
            throw new IllegalStateException("You do not have permissions to edit this board.");
        }
        mainCtrl.showSettings();
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

    @FXML
    public void initialize() {
        root.addEventFilter(KeyEvent.KEY_PRESSED, new MainViewKeyEventHandler(client));
    }

    public void loadData(Board board) {
        boolean admin = server.isAdmin();
        if (!admin) {
            adminLabel.setVisible(false);
        }

        BoardCtrl boardCtrl = factory.create(BoardCtrl.class, board);
        boardContainer.setContent(boardCtrl.getNode());
        displayBoardName.setText(board.getName());
        warning.setVisible(!board.isEditable());
        registerForMessages();
    }

    /**
     * This method is used after loading the data in order to subscribe the client to different endpoints
     * and handle events related to deleting, creating, and updating cards and lists on the current board the
     * user is viewing.
     */
    public void registerForMessages() {

        long boardId = client.getBoardCtrl().getBoard().getId();

        /**
         * This method call handles the deletion,addition and updating of a card on the current board by
         * using the registerForMessages method of the server, which refreshes the CardListCtrl of the card
         * in question.
         */
        server.registerForMessages("/topic/board/" + boardId + "/cards", Card.class, c -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    client.getCardListCtrl(c.getCardList().getId()).refresh();
                    mainCtrl.getActiveCtrl().revalidate();
                }
            });

        });

        /**
         * This method call handles the deletion,addition and updating of a list on the current board by
         * using the registerForMessages method of the server, which refreshes the BoardCtrl of the list
         * in question.
         */
        server.registerForMessages("/topic/board/" + boardId + "/lists", CardList.class, l -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    client.getBoardCtrl().refresh();
                    mainCtrl.getActiveCtrl().revalidate();
                }
            });
        });

        /**
         * This method call is used for informing the client that the board they are currently on
         * has been deleted
         */
        server.registerForMessages("/topic/board/" + boardId + "/delete", Board.class, b -> {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    client.setBoardCtrl(null);
                    mainCtrl.showJoin();
                    exceptionHandler.clientException("Sorry, but the board you are currently viewing has been permanently deleted.");
                }
            });
        });
    }
    @Override
    public void revalidate() {

    }
}
