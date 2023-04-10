package client.components;

import client.scenes.JoinBoardsCtrl;
import client.scenes.MainCtrl;
import client.utils.ClientPreferences;
import client.utils.ClientUtils;
import client.utils.Logger;
import client.utils.ServerUtils;
import commons.Board;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javax.inject.Inject;

public class BoardJoinCtrl implements Component<Board> {
    private final MainCtrl mainCtrl;
    private final JoinBoardsCtrl joinBoardsCtrl;
    private final ClientPreferences clientPreferences;

    private final ClientUtils client;
    @FXML
    private Label label;

    @FXML
    private AnchorPane pane;
    @FXML
    private ImageView lockImage;
    @FXML
    private Button leave;
    private Board board;

    private ServerUtils server;

    @Override
    public void loadData(Board board) {
        this.board = board;
        if (board.getPassword() == null) lockImage.setVisible(false);
        if(server.isAdmin())
            leave.setVisible(false);
        label.setText(board.getName());
        pane.setStyle("-fx-background-color: " + board.getBoardColor());
        label.setStyle("-fx-text-fill: #000000ff");
    }

    @Override
    public Parent getNode() {
        return pane;
    }

    @Inject
    public BoardJoinCtrl(MainCtrl mainCtrl, JoinBoardsCtrl joinBoardsCtrl, ServerUtils server,
                         ClientPreferences clientPreferences, ClientUtils client) {
        this.mainCtrl = mainCtrl;
        this.joinBoardsCtrl = joinBoardsCtrl;
        this.server = server;
        this.clientPreferences = clientPreferences;
        this.client = client;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BoardJoin.fxml"));
        loader.setController(this);
        loader.setRoot(this);
    }

    public void onSelect(ActionEvent actionEvent) {
        String password = clientPreferences.getPasswordForBoard(board.getId()).orElse(null);
        if (password != null) Logger.log("Detected local password for board " + board.getId() + ": " + password);
        boolean passwordExists = board.getPassword() != null;
        boolean validSavedPassword = passwordExists && board.getPassword().equals(password);
        if (validSavedPassword) Logger.log("Saved password is valid.");
        if (server.isAdmin()) Logger.log("Admin mode enabled- bypassing password check.");
        if (passwordExists && !validSavedPassword && !server.isAdmin()) {
            joinBoardsCtrl.requestPassword(board);
            return;
        }
        joinBoardsCtrl.stopPolling();
        mainCtrl.getMainViewCtrl().unsubscribe();
        mainCtrl.showMainView(board);
    }

    public void onRemove() {
        clientPreferences.removeJoinedBoard(this.board.getId());
        if (client.getBoardCtrl() != null && client.getBoardCtrl().getBoard().getId() == this.board.getId()) {
            mainCtrl.getMainViewCtrl().unsubscribe();
        }
        joinBoardsCtrl.stopPolling();
        joinBoardsCtrl.populateBoards();
    }
}
