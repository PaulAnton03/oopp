package client.components;

import client.scenes.JoinBoardsCtrl;
import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.Board;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import javax.inject.Inject;

public class BoardJoinCtrl implements Component<Board> {
    @FXML
    private Button button;

    private Board board;

    private final MainCtrl mainCtrl;
    private final JoinBoardsCtrl joinBoardsCtrl;

    private ServerUtils server;

    @Override
    public void loadData(Board board) {
        this.board = board;
        button.setText(board.getName());
    }

    @Override
    public Parent getNode() {
        return button;
    }

    @Inject
    public BoardJoinCtrl(MainCtrl mainCtrl, JoinBoardsCtrl joinBoardsCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.joinBoardsCtrl = joinBoardsCtrl;
        this.server = server;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BoardJoin.fxml"));
        loader.setController(this);
        loader.setRoot(this);
    }

    public void onSelect(ActionEvent actionEvent) {
        if (board.getPassword() != null && !server.isAdmin()) {
            joinBoardsCtrl.requestPassword(board);
            return;
        }
        mainCtrl.showMainView(board);
    }
}
