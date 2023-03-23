package client.components;

import client.scenes.JoinBoardsCtrl;
import client.scenes.MainCtrl;
import commons.Board;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

import javax.inject.Inject;

public class BoardJoinCtrl {
    @FXML
    private Button button;

    private Board board;

    private MainCtrl mainCtrl;
    private JoinBoardsCtrl joinBoardsCtrl;

    public void loadData(Board b) {
        this.board = b;
        button.setText(b.getName());
    }

    @Inject
    public BoardJoinCtrl(MainCtrl mainCtrl, JoinBoardsCtrl joinBoardsCtrl) {
        this.mainCtrl = mainCtrl;
        this.joinBoardsCtrl = joinBoardsCtrl;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BoardJoin.fxml"));
        loader.setController(this);
        loader.setRoot(this);
    }

    public void onSelect(ActionEvent actionEvent) {
        if (board.getPassword() != null) {
            joinBoardsCtrl.requestPassword(board);
            return;
        }
        mainCtrl.showMainView(board);
    }
}
