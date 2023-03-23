package client.components;

import client.scenes.MainCtrl;
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

    private MainCtrl mainCtrl;

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
    public BoardJoinCtrl(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BoardJoin.fxml"));
        loader.setController(this);
        loader.setRoot(this);
    }

    public void onSelect(ActionEvent actionEvent) {
        System.out.println("Selected board " + board);
        mainCtrl.showMainView(board);
    }
}
