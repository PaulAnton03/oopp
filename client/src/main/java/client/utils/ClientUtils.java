package client.utils;

import commons.Board;
import javax.inject.Inject;
import lombok.Setter;

import java.util.Optional;

public class ClientUtils {
    private ServerUtils server;
    private ClientPreferences preferences;
    @Setter
    private Board selectedBoard;

    @Inject
    public ClientUtils(ServerUtils server, ClientPreferences preferences) {
        this.server = server;
        this.preferences = preferences;
    }

    public Board getSelectedBoard() {
        if (selectedBoard == null) {
            Optional<Long> boardId = preferences.getDefaultBoardId();
            selectedBoard = server.getBoardTest(boardId.orElse(0L));
        }
        return selectedBoard;
    }
}
