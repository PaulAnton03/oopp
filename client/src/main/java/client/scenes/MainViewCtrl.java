package client.scenes;

import client.components.BoardCtrl;
import client.utils.ClientUtils;
import client.utils.ComponentFactory;
import client.utils.MainViewKeyEventHandler;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.CardList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import lombok.Getter;
import lombok.Setter;


public class MainViewCtrl {

    private final ServerUtils server;
    private final ClientUtils client;
    private final MainCtrl mainCtrl;
    private final ComponentFactory factory;

    @Getter
    @Setter
    private BoardCtrl boardCtrl;

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
    public MainViewCtrl(ServerUtils server, ClientUtils client, MainCtrl mainCtrl, ComponentFactory factory) {
        this.server = server;
        this.client = client;
        this.mainCtrl = mainCtrl;
        this.factory = factory;
    }

    @FXML
    void btnAddClicked(ActionEvent event) {
        if(!client.getActiveBoard().isEditable()) {
            throw new IllegalStateException("You do not have permissions to edit this board.");
        }
        else if (client.getActiveBoardCtrl() != null)
            mainCtrl.showAddList();
        else {
            throw new IllegalStateException("You cannot add lists to the empty board. Please select a board to operate on");
        }
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
        if(!client.getActiveBoard().isEditable()) {
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
        root.addEventFilter(KeyEvent.KEY_PRESSED, new MainViewKeyEventHandler(this));
    }

    public void loadData(Board board) {
        boolean admin = server.isAdmin();
        if(!admin) {
            adminLabel.setVisible(false);
        }

        this.boardCtrl = factory.create(BoardCtrl.class, board);
        client.setActiveBoardCtrl(boardCtrl);
        boardContainer.setContent(boardCtrl.getNode());
        displayBoardName.setText(board.getName());
        server.registerForMessages("/topic/lists", CardList.class, l -> {
            if (client.getActiveBoard().getId() == l.getBoard().getId()) {
                System.out.println("INCOMING LIST: " + l);
            }
        });
        server.registerForMessages("/topic/cards", Card.class, c -> {
            if (c.getCardList().getBoard().getId() == client.getActiveBoard().getId()) {
                System.out.println("INCOMING CARD: " + c);
            }
        });
        warning.setVisible(!board.isEditable());
    }
}
