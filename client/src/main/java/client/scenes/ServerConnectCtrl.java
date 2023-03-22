package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ServerConnectCtrl {

    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    @FXML
    private TextField serverInput;

    @Inject
    public ServerConnectCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = server;
    }

    public void connect() {
        String server = serverInput.getText();
        if (server == null || server.isEmpty()) {
            server = serverUtils.getServerPath();
        }

        // Connect to server
        serverUtils.setServerPath(server);
        serverUtils.connect();
        System.out.println("Connecting to server: " + server);

        /* Original code, altering it in order to create a test for auto generation.
        // Switching the scene
        mainCtrl.showMainView();

         */

        mainCtrl.getPrimaryStage().setResizable(true);
        mainCtrl.showMainView(generateTestBoard());
    }

    /**
     * Test method for auto generating boards. Make sure to delete this once we are sure generation works properly.
     * Also revert changes from above
     * @return testBoard for testing
     */
    public Board generateTestBoard() {
        Card card1 = new Card("Id:1", "Desc:1");
        Card card2 = new Card("Id:2", "Desc:2");
        CardList cardList1 = new CardList("TestList1");
        cardList1.addCard(card1);cardList1.addCard(card2);
        CardList cardList2 = new CardList("TestList1");
        cardList2.addCard(card1);cardList2.addCard(card2);cardList2.addCard(card1);
        Board board = new Board();
        board.addCardList(cardList1);board.addCardList(cardList2);
        return board;
    }
}
