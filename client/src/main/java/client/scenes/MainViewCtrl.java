package client.scenes;

import client.utils.ClientUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.CardList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class MainViewCtrl {

    private final ServerUtils server;
    private final ClientUtils client;
    private final MainCtrl mainCtrl;

    @FXML private HBox boardView;
    @FXML private Text displayBoardName;

    @Inject
    public MainViewCtrl(ServerUtils server, ClientUtils client, MainCtrl mainCtrl) {
        this.server = server;
        this.client = client;
        this.mainCtrl = mainCtrl;
    }

    @FXML
    void btnAddClicked(ActionEvent event) { mainCtrl.showAddCard(); }

    @FXML
    void btnBackClicked(ActionEvent event) { mainCtrl.showConnect(); }

    @FXML
    void btnCreateClicked(ActionEvent event) { mainCtrl.showCreate(); }

    @FXML
    void btnJoinClicked(ActionEvent event) { mainCtrl.showJoin(); }

    @FXML
    void btnSettingsClicked(ActionEvent event) { mainCtrl.showSettings(); }

    /**
     * Initializes the main view with the board provided
     * @param board board for which card lists are displayed. If null, empty board is displayed.
     */
    void onSetup(Board board) {
        // Clear everything
        boardView.getChildren().clear();

        if (board == null) {
            board = new Board("Empty board");
        } else {
            client.setActiveBoard(board);
        }
        displayBoardName.setText(board.getName());

        for (CardList cardList : board.getCardLists()) {
            boardView.getChildren().add(mainCtrl.createCardList(cardList));
        }
    }
}
