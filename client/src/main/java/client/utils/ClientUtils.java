package client.utils;

import commons.Board;
import javax.inject.Inject;

import commons.CardList;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

public class ClientUtils {
    private final ServerUtils server;
    private final ClientPreferences preferences;
    @Setter
    private Board activeBoard;
    @Getter
    @Setter
    private CardList activeCardList;

    @Inject
    public ClientUtils(ServerUtils server, ClientPreferences preferences) {
        this.server = server;
        this.preferences = preferences;
    }

    public Board getActiveBoard() {
        if (activeBoard == null) {
            Optional<Long> boardId = preferences.getDefaultBoardId();
            activeBoard = boardId.isPresent() ?  server.getBoard(boardId.get()) : null;
        } else {
            activeBoard = server.getBoard(activeBoard.getId());
        }
        return activeBoard;
    }
}
