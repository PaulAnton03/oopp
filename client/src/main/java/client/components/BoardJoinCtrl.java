package client.components;

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

    public void loadData(Board b) {
        this.board = b;
        button.setText(b.getName());
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
