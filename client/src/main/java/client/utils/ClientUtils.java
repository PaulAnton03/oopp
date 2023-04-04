package client.utils;

import java.util.HashMap;
import java.util.Map;

import client.components.BoardCtrl;
import client.components.CardCtrl;
import client.components.CardListCtrl;
import client.components.SubTaskCtrl;
import commons.Card;
import commons.CardList;
import commons.SubTask;
import lombok.Getter;
import lombok.Setter;

public class ClientUtils {
    @Getter
    @Setter
    private long activeCardListId;
    @Getter
    @Setter
    private long selectedCardId = -1;

    @Getter
    @Setter
    private BoardCtrl boardCtrl;
    @Getter
    @Setter
    private Map<Long, CardListCtrl> cardListCtrls = new HashMap<>();
    @Getter
    @Setter
    private Map<Long, CardCtrl> cardCtrls = new HashMap<>();

    @Getter
    @Setter
    private Map<Long, SubTaskCtrl> subTaskCtrls = new HashMap<>();


    public void clearBoardData() {
        this.cardListCtrls = new HashMap<>();
        this.cardCtrls = new HashMap<>();
        this.subTaskCtrls = new HashMap<>();
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

    public SubTask getSubTask(long id) {
        return subTaskCtrls.get(id).getSubTask();
    }

    public SubTaskCtrl getSubTaskCtrl(long id) {
        return subTaskCtrls.get(id);
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

    public void changeSelection(long selectedCardId) {
        CardCtrl cardCtrl = getCardCtrl(this.selectedCardId);
        if (cardCtrl != null)
            cardCtrl.unhighlight();
        this.selectedCardId = selectedCardId;
        getCardCtrl(selectedCardId).highlight();
    }
}
