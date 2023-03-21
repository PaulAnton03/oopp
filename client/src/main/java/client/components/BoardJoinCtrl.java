package client.components;

import client.scenes.MainCtrl;
import client.utils.ServerUtils;
import commons.Board;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

public class BoardJoinCtrl {
    @FXML
    private Button button;

    private Board board;

    private MainCtrl mainCtrl;

    public void loadData(MainCtrl mainCtrl, Board b) {
        this.mainCtrl = mainCtrl;
        this.board = b;
        button.setText(b.getName());
    }

    public BoardJoinCtrl() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("BoardJoin.fxml"));
        loader.setController(this);
        loader.setRoot(this);
    }

    public void onSelect(ActionEvent actionEvent) {
        System.out.println("Selected board " + board);
        mainCtrl.showMainView(board);
    }
}
