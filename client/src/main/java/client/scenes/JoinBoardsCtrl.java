package client.scenes;

import javax.inject.Inject;

import client.Main;
import client.MyFXML;
import client.components.BoardJoinCtrl;
import client.utils.ServerUtils;
import commons.Board;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class JoinBoardsCtrl {

    private final ServerUtils server;

    private final MainCtrl mainCtrl;

    private static MyFXML myFXML = Main.getFXML();

    @FXML
    private TextField boardPasswordField;
    @FXML
    private Button btnClear;
    @FXML
    private Button btnOk;
    @FXML
    private Button navbarBackButton;
    @FXML
    private Button navbarCreateButton;

    @FXML
    private VBox boardPopulation;

    public void populateBoards() {
        for (Board b : server.getBoards()) {
            var pair = myFXML.load(BoardJoinCtrl.class, "client", "components", "BoardJoin.fxml");
            BoardJoinCtrl ctrl = pair.getKey();
            var board = pair.getValue();

            ctrl.loadData(mainCtrl, b);

            boardPopulation.getChildren().add(board);
        }
    }

    @Inject
    public JoinBoardsCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @FXML
    void addCardClicked(ActionEvent event) {
        System.out.println("Password set: " + boardPasswordField.getText());
    }

    @FXML
    void btnBackClicked(ActionEvent event) {
        boardPasswordField.setText("");
        mainCtrl.showMainView();
    }

    @FXML
    void btnClearClicked(ActionEvent event) {
        boardPasswordField.setText("");
        System.out.println("Cleared!");
    }

    @FXML
    void btnCreateClicked(ActionEvent event) {
        mainCtrl.showCreate();
    }

}
