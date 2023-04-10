package client.utils;

import java.util.HashMap;
import java.util.Map;

import client.components.BoardCtrl;
import client.components.CardCtrl;
import client.components.CardListCtrl;
import client.components.TagCtrl;
import client.components.SubTaskCtrl;
import commons.Card;
import commons.CardList;
import commons.Tag;
import commons.SubTask;
import lombok.Getter;
import lombok.Setter;

public class ClientUtils {
    @Getter
    @Setter
    private long activeCardListId;
    @Getter
    @Setter
    private String editedCardTitle; /* Always set to null when not editing a title */
    @Getter
    @Setter
    private int caretPosition;
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


    @Getter
    @Setter
    private  Map<Long, TagCtrl> tagCtrls = new HashMap<>();

    public void clearBoardData() {
        this.cardListCtrls = new HashMap<>();
        this.cardCtrls = new HashMap<>();
        this.tagCtrls = new HashMap<>();
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

    public Tag getTag(long id){
        return tagCtrls.get(id).getTag();
    }

    public TagCtrl getTagCtrl(long id){
        return tagCtrls.get(id);
    }

    public void changeSelection(long selectedCardId) {
        if (selectedCardId == this.selectedCardId)
            return;
        CardCtrl cardCtrl = getCardCtrl(this.selectedCardId);
        if (cardCtrl != null)
            cardCtrl.unhighlight();
        this.selectedCardId = selectedCardId;
        this.editedCardTitle = null;
        getCardCtrl(selectedCardId).highlight();
    }

    public void postRefresh() {
        if (getCardCtrl(getSelectedCardId()) != null
            && getEditedCardTitle() != null)
            getCardCtrl(getSelectedCardId()).getTitleField().requestFocus();
    }
}
