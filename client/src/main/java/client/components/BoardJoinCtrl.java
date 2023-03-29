package client.components;

import client.scenes.JoinBoardsCtrl;
import client.scenes.MainCtrl;
import client.utils.ClientPreferences;
import client.utils.Logger;
import client.utils.ServerUtils;
import commons.Board;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import javax.inject.Inject;

public class BoardJoinCtrl implements Component<Board> {
    private final MainCtrl mainCtrl;
    private final JoinBoardsCtrl joinBoardsCtrl;
    private final ClientPreferences clientPreferences;
    @FXML
    private Label label;

    @FXML
    private AnchorPane pane;
    @FXML
    private ImageView lockImage;
    private Board board;

    private ServerUtils server;

    @Override
    public void loadData(Board board) {
        this.board = board;
        if(board.getPassword() == null) lockImage.setVisible(false);
        label.setText(board.getName());
    }

    @Override
    public Parent getNode() {
        return pane;
    }

    @Inject
    public BoardJoinCtrl(MainCtrl mainCtrl, JoinBoardsCtrl joinBoardsCtrl, ServerUtils server, ClientPreferences clientPreferences) {
        this.mainCtrl = mainCtrl;
        this.joinBoardsCtrl = joinBoardsCtrl;
        this.server = server;
        this.clientPreferences = clientPreferences;
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
        mainCtrl.showMainView(board);
    }
}
