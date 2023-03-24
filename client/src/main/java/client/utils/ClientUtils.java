package client.utils;

import client.components.BoardCtrl;
import client.components.CardCtrl;
import client.components.CardListCtrl;
import commons.Board;
import commons.Card;
import commons.CardList;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;

public class ClientUtils {
    private final ServerUtils server;
    private final ClientPreferences preferences;
    private final ComponentFactory factory;

    @Setter
    private BoardCtrl activeBoardCtrl;
    @Getter
    @Setter
    private CardListCtrl activeCardListCtrl;
    @Getter
    @Setter
    private CardCtrl activeCardCtrl;

    @Inject
    public ClientUtils(ServerUtils server, ClientPreferences preferences, ComponentFactory factory) {
        this.server = server;
        this.preferences = preferences;
        this.factory = factory;
    }

    public BoardCtrl getActiveBoardCtrl() {
        if (activeBoardCtrl == null) {
            // Load default board
            preferences.getDefaultBoardId().ifPresent(boardId -> {
                activeBoardCtrl = factory.create(BoardCtrl.class, server.getBoard(boardId));
            });
        }
        return activeBoardCtrl;
    }

    public Board getActiveBoard() {
        if (getActiveBoardCtrl() == null) return null;
        return getActiveBoardCtrl().getBoard();
    }

    public CardList getActiveCardList() {
        if (getActiveCardListCtrl() == null) return null;
        return getActiveCardListCtrl().getCardList();
    }

    public Card getActiveCard() {
        if (getActiveCardCtrl() == null) return null;
        return getActiveCardCtrl().getCard();
    }
}
