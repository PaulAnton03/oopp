package client.scenes;

import client.components.CardCtrl;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

public class MainViewCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML private HBox boardView;

    @Inject
    public MainViewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
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

        // System.out.println("Displaying board " + board.getId());
        // final List<CardList> cardLists = board == null ? new ArrayList<>() : board.getCardLists();



        var pair = mainCtrl.createNewCard();
        CardCtrl cardCtrl = pair.getKey();
        var card = pair.getValue();

        cardCtrl.loadData(new Card("My title " + Math.floor(Math.random() * 100), "This is a description of this task......"));

        boardView.getChildren().add(card);

        cardCtrl.loadData(new Card("This has chanded " + Math.floor(Math.random() * 100), "This is a description of this task......"));


    }
}
