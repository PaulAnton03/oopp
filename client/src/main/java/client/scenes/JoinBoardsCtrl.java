package client.scenes;

import client.components.BoardJoinCtrl;
import client.utils.ClientPreferences;
import client.utils.ClientUtils;
import client.utils.ComponentFactory;
import client.utils.ServerUtils;
import commons.Board;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.Response;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.glassfish.jersey.client.ClientConfig;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;

public class JoinBoardsCtrl implements SceneCtrl {

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
    //TODO: Join Board based on Id. Don't forget to unsubscribe from
    // current board if there is one

    private Thread worker;

    private final AtomicBoolean running = new AtomicBoolean(false);

    private List<Board> currentBoards = new ArrayList<>();


    private void startPolling() {
        running.set(true);
        worker = new Thread(() -> {
            while (running.get()) {
                var res = ClientBuilder.newClient(new ClientConfig())
                        .target("http://" + server.getServerPath() + "/boards/updates")
                        .request(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .get(Response.class);
                if (res.getStatus() == 204)
                    continue;
                List<Board> updatedBoards;
                if (server.isAdmin()) {
                    updatedBoards = server.getBoards();
                } else {
                    updatedBoards = server.getBoards().stream()
                            .filter(b -> clientPrefs.containsJoinedBoard(b.getId())).collect(Collectors.toList());
                }
                Platform.runLater(() -> updateBoards(updatedBoards));
            }
        });
        worker.start();
    }

    private void updateBoards(List<Board> updatedBoards) {
        boardPopulation.getChildren().clear();
        var boardJoinNodes = updatedBoards.stream()
                .map(board -> factory.create(BoardJoinCtrl.class, board).getNode())
                .collect(Collectors.toList());
        boardPopulation.getChildren().addAll(boardJoinNodes);
    }

    public void stopPolling() {
        running.set(false);
    }

    public void populateBoards() {
        boardPopulation.getChildren().clear();
        if (server.isAdmin()) {
            var boardJoinNodes = server.getBoards().stream()
                    .map(board -> factory.create(BoardJoinCtrl.class, board).getNode())
                    .collect(Collectors.toList());
            boardPopulation.getChildren().addAll(boardJoinNodes);
            startPolling();
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
        startPolling();
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
        stopPolling();
        if (client.getBoardCtrl() == null) {
            mainCtrl.showConnect();
            return;
        }
        mainCtrl.showMainView();
    }

    public void btnCreateClicked() {
        stopPolling();
        mainCtrl.showCreate();
    }

    public void requestPassword(Board pswProtectedBoard) {
        stopPolling();
        mainCtrl.showPasswordProtected(pswProtectedBoard);
    }

    public void onJoin(ActionEvent event) {
        stopPolling();
        String inviteKey = boardId.getText();
        if (inviteKey.isEmpty()) {
            throw new RuntimeException("The invite key cannot be empty");
        }

        if (!inviteKey.matches("^join-talio#.{5,30}#[0-9]+(?:#.+)?$")) {
            throw new RuntimeException("The invite key is invalid, maybe copy it again");
        }
        String[] parts = inviteKey.split("#");
        final long boardId = Long.parseLong(parts[2]);

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
