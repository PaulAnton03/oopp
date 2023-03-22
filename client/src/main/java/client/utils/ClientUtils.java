package client.utils;

import commons.Board;
import javax.inject.Inject;

import commons.CardList;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

public class ClientUtils {
    @Getter
    private final ServerUtils server;
    private final ClientPreferences preferences;
    @Setter
    private Board activeBoard;
    @Setter
    private CardList activeCardList;

    @Inject
    public ClientUtils(ServerUtils server, ClientPreferences preferences) {
        this.server = server;
        this.preferences = preferences;
    }

    public Board getActiveBoard() {
        if (activeBoard == null || activeBoard.getName().equals("Empty Board")) {
            Optional<Long> boardId = preferences.getDefaultBoardId();
            return boardId.isPresent() ? server.getBoard(boardId.get()) : null;
        } else {
            activeBoard = server.getBoard(activeBoard.getId());
            return activeBoard;
        }
    }

    public CardList getActiveCardList() {
        if (activeCardList == null) {
            activeCardList = getActiveBoard().getCardLists().stream().findFirst().orElseGet(null);
        }
        return activeCardList;
    }
}
