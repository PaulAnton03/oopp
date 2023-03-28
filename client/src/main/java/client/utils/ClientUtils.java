package client.utils;

import client.components.BoardCtrl;
import client.components.CardCtrl;
import client.components.CardListCtrl;
import commons.Board;
import commons.Card;
import commons.CardList;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class ClientUtils {
    private final ServerUtils server;
    private final ClientPreferences preferences;

    @Setter
    private BoardCtrl activeBoardCtrl;
    @Getter
    @Setter
    private CardListCtrl activeCardListCtrl;
    @Getter
    @Setter
    private CardCtrl activeCardCtrl;

    @Getter
    @Setter
    private Map<Long, CardListCtrl> cardListCtrls = new HashMap<>();
    @Getter
    @Setter
    private Map<Long, CardCtrl> cardCtrls = new HashMap<>();

    @Inject
    public ClientUtils(ServerUtils server, ClientPreferences preferences) {
        this.server = server;
        this.preferences = preferences;
    }

    public BoardCtrl getActiveBoardCtrl() {
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
