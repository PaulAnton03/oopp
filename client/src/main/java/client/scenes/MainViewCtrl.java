package client.scenes;

import client.utils.ClientUtils;
import client.utils.Logger;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.CardList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class MainViewCtrl {

    private final ServerUtils server;
    private final ClientUtils client;
    private final MainCtrl mainCtrl;

    @FXML
    private HBox boardView;
    @FXML
    private Text displayBoardName;

    @Inject
    public MainViewCtrl(ServerUtils server, ClientUtils client, MainCtrl mainCtrl) {
        this.server = server;
        this.client = client;
        this.mainCtrl = mainCtrl;
    }

    @FXML
    void btnAddClicked(ActionEvent event) {
        if (client.getActiveBoard() != null)
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

    /**
     * Initializes the main view with the board provided
     *
     * @param board board for which card lists are displayed. If null, empty board is displayed.
     */
    void onSetup(Board board) {
        // Clear everything
        boardView.getChildren().clear();

        if (board == null) {
            mainCtrl.showJoin();
            Logger.log("No board selected, displaying join screen", Logger.LogLevel.WARN);
            return;
        }
        client.setActiveBoard(board);
        displayBoardName.setText(board.getName());

        for (CardList cardList : board.getCardLists()) {
            boardView.getChildren().add(mainCtrl.createCardList(cardList));
        }
        Logger.log("Main view initialized with board " + board.getName(), Logger.LogLevel.INFO);
    }
}
