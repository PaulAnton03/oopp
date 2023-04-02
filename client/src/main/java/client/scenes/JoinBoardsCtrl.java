package client.scenes;

import client.components.BoardJoinCtrl;
import client.utils.ClientPreferences;
import client.utils.ClientUtils;
import client.utils.ComponentFactory;
import client.utils.ServerUtils;
import commons.Board;
import jakarta.ws.rs.NotFoundException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.stream.Collectors;

public class JoinBoardsCtrl {

    private final ClientUtils client;
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
        if(server.isAdmin()) {
            var boardJoinNodes = server.getBoards().stream()
                    .map(board -> factory.create(BoardJoinCtrl.class, board).getNode())
                    .collect(Collectors.toList());
            boardPopulation.getChildren().addAll(boardJoinNodes);
            return;
        }
        try {
            var boardJoinNodes = clientPrefs.getJoinedBoards().stream()
                    .map(boardId -> factory.create(BoardJoinCtrl.class, server.getBoard(boardId)).getNode())
                    .collect(Collectors.toList());
            boardPopulation.getChildren().addAll(boardJoinNodes);
        } catch (NotFoundException e) {
            clientPrefs.clearPreferences();
        }
    }

    @Inject
    public JoinBoardsCtrl(ClientUtils client, ServerUtils server, MainCtrl mainCtrl, ComponentFactory factory, ClientPreferences clientPrefs) {
        this.client = client;
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.factory = factory;
        this.clientPrefs = clientPrefs;
    }

    public void btnBackClicked() {
        if (client.getBoardCtrl() == null) {
            mainCtrl.showConnect();
            return;
        }
        mainCtrl.showMainView();
    }

    public void btnCreateClicked() {
        mainCtrl.showCreate();
    }

    public void requestPassword(Board pswProtectedBoard) {
        mainCtrl.showPasswordProtected(pswProtectedBoard);
    }

    public void onJoin(ActionEvent event) {
        String id = this.boardId.getText();
        long boardId;
        try {
            boardId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Board ID must be a number");
        }

        try {
            server.getBoard(boardId);
        } catch (Exception e) {
            throw new IllegalArgumentException("Board with ID " + boardId + " does not exist");
        }

        if (clientPrefs.getJoinedBoards().contains(boardId))
            throw new IllegalArgumentException("You have already joined to this board");

        clientPrefs.addJoinedBoard(boardId);
        populateBoards();
        this.boardId.setText("");
    }
}
