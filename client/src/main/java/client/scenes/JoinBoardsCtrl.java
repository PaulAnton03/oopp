package client.scenes;

import client.components.BoardJoinCtrl;
import client.utils.ComponentFactory;
import client.utils.ServerUtils;
import commons.Board;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.stream.Collectors;

public class JoinBoardsCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final ComponentFactory factory;

    @FXML
    private VBox boardPopulation;

    @FXML
    private TextField boardId;

    @FXML
    private Button btnJoin;

    public void populateBoards() {
        boardPopulation.getChildren().clear();

        var boardJoinNodes = server.getBoards().stream()
                .map(board -> factory.create(BoardJoinCtrl.class, board).getNode())
                .collect(Collectors.toList());
        boardPopulation.getChildren().addAll(boardJoinNodes);
    }

    @Inject
    public JoinBoardsCtrl(ServerUtils server, MainCtrl mainCtrl, ComponentFactory factory) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.factory = factory;
    }

    public void btnBackClicked() {
        mainCtrl.showMainView();
    }

    public void btnCreateClicked() {
        mainCtrl.showCreate();
    }

    public void requestPassword(Board pswProtectedBoard) {
        mainCtrl.showPasswordProtected(pswProtectedBoard);
    }

    public void onJoin() {
        
    }
}
