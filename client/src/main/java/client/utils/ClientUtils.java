package client.utils;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import client.components.BoardCtrl;
import client.components.CardCtrl;
import client.components.CardListCtrl;
import commons.Board;
import commons.Card;
import commons.CardList;
import lombok.Getter;
import lombok.Setter;

public class ClientUtils {
    private final ServerUtils server;
    private final ClientPreferences preferences;

    @Getter
    @Setter
    private long activeCardListId;

    @Getter
    @Setter
    private BoardCtrl boardCtrl;
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

    public void clearBoardData() {
        this.cardListCtrls = new HashMap<>();
        this.cardCtrls = new HashMap<>();
        this.boardCtrl = null;
    }

    public CardListCtrl getActiveCardListCtrl() {
        return cardListCtrls.get(activeCardListId);
    }

    public CardList getActiveCardList() {
        return cardListCtrls.get(activeCardListId).getCardList();
    }

    public CardList getCardList(long id) {
        return cardListCtrls.get(id).getCardList();
    }

    public Card getCard(long id) {
        return cardCtrls.get(id).getCard();
    }

    public CardListCtrl getCardListCtrl(long id) {
        return cardListCtrls.get(id);
    }

    public CardCtrl getCardCtrl(long id) {
        return cardCtrls.get(id);
    }
}
