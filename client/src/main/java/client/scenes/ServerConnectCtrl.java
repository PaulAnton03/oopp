package client.scenes;

import client.utils.Logger;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.util.concurrent.CompletableFuture;

public class ServerConnectCtrl {

    private final ServerUtils serverUtils;
    private final MainCtrl mainCtrl;
    @FXML
    private TextField serverInput;
    @FXML
    private TextField boardInput;

    @Inject
    public ServerConnectCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = server;
    }

    public void connect() {
        String serverPath = serverInput.getText();

        if (!serverPath.isEmpty()) {
            if (!handleCustomServer(serverPath)) {
                return;
            }
        }

        connectToServer();
        // TODO: Implement connecting to a specific board.
    }

    private boolean handleCustomServer(String serverPath) {
        boolean admin = serverPath.startsWith("admin//");
        if (admin) {
            serverPath = serverPath.substring(7);
        }
        serverUtils.setAdmin(admin);
        serverUtils.setServerPath(serverPath);

        if (admin) {
            askForAdminPassword();
            return false;
        }
        return true;
    }

    private void askForAdminPassword() {
        CompletableFuture<Boolean> correctPassword = mainCtrl.getAdminPasswordCtrl().loadFuture();
        mainCtrl.showAdminPasswordProtected();
        correctPassword.thenAccept((correct) -> {
            if (correct) {
                connectToServer();
            }
        });
    }

    private void connectToServer() {
        mainCtrl.showMainView();
        // Connect to server
        serverUtils.connect();
        Logger.log("Connecting to server: " + serverUtils.getServerPath());
    }

    /**
     * Test method for auto generating boards. Make sure to delete this once we are sure generation works properly.
     * Also revert changes from above
     *
     * @return testBoard for testing
     */
    public Board generateTestBoard() {
        Card card1 = new Card("Id:1", "Desc:1");
        Card card2 = new Card("Id:2", "Desc:2");
        CardList cardList1 = new CardList("TestList1");
        cardList1.addCard(card1);
        cardList1.addCard(card2);
        CardList cardList2 = new CardList("TestList1");
        cardList2.addCard(card1);
        cardList2.addCard(card2);
        cardList2.addCard(card1);
        Board board = new Board("Empty Board");
        board.addCardList(cardList1);
        board.addCardList(cardList2);
        return board;
    }
}
