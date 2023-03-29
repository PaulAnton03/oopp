package client.scenes;

import client.components.BoardJoinCtrl;
import client.utils.ClientPreferences;
import client.utils.ComponentFactory;
import client.utils.ServerUtils;
import commons.Board;
import javafx.event.ActionEvent;
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
    private final ClientPreferences clientPrefs;

    @FXML
    private VBox boardPopulation;

    @FXML
    private TextField boardId;

    @FXML
    private Button btnJoin;

    public void populateBoards() {
        boardPopulation.getChildren().clear();

        /* Commenting original code in case changes go horribly wrong
        var boardJoinNodes = server.getBoards().stream()
                .map(board -> factory.create(BoardJoinCtrl.class, board).getNode())
                .collect(Collectors.toList());*/
        var boardJoinNodes = clientPrefs.getJoinedBoards().stream()
                .map(boardId -> factory.create(BoardJoinCtrl.class, server.getBoard(boardId)).getNode())
                .collect(Collectors.toList());
        boardPopulation.getChildren().addAll(boardJoinNodes);
    }

    @Inject
    public JoinBoardsCtrl(ServerUtils server, MainCtrl mainCtrl, ComponentFactory factory, ClientPreferences clientPrefs) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.factory = factory;
        this.clientPrefs = clientPrefs;
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

    public void onJoin(ActionEvent event) {
        String id = boardId.getText();
        long boardId;
        try {
            boardId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Board ID must be a number");
        }

        Board board;
        try {
            board = server.getBoard(boardId);
        } catch (Exception e) {
            throw new IllegalArgumentException("Board with ID " + boardId + " does not exist");
        }

        if (board.getPassword() != null) {
            requestPassword(board);
            return;
        }

        mainCtrl.showMainView(board);
    }
}
