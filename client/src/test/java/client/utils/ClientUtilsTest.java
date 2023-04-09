package client.utils;

import client.components.CardCtrl;
import client.components.CardListCtrl;
import commons.Card;
import commons.CardList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientUtilsTest {
    private ClientUtils utils;
    private static class CardCtrlDummy extends CardCtrl {
        private final Card dummyCard;
        public CardCtrlDummy(Card dummyCard) {
            super(null, null, null, null);
            this.dummyCard = dummyCard;
        }

        @Override public Card getCard() { return dummyCard; }
        @Override public void highlight() { }
        @Override public void unhighlight() { }
    }
    private static class CardListCtrlDummy extends CardListCtrl {
        private final CardList dummyCardList;

        public CardListCtrlDummy(CardList dummyCardList) {
            super(null, null, null, null);
            this.dummyCardList = dummyCardList;
        }

        @Override
        public CardList getCardList() { return dummyCardList; }
    }
    private static final Card sampleCard = new Card("title", "description");
    private static final CardList sampleCardList = new CardList("title");
    private static final CardCtrlDummy sampleCardCtrl = new CardCtrlDummy(sampleCard);
    private static final CardListCtrlDummy sampleCardListCtrl = new CardListCtrlDummy(sampleCardList);

    @BeforeEach
    public void setUp() {
        utils = new ClientUtils();
        utils.getCardListCtrls().put(1L, sampleCardListCtrl);
        utils.getCardCtrls().put(1L, sampleCardCtrl);
        utils.getCardCtrls().put(2L, sampleCardCtrl);
    }

    @Test
    public void ctrlsStoreTest() {
        assertEquals(sampleCardList, utils.getCardList(1L));
        assertEquals(sampleCardListCtrl, utils.getCardListCtrl(1L));

        assertEquals(sampleCard, utils.getCard(1L));
        assertEquals(sampleCardCtrl, utils.getCardCtrl(1L));
    }

    @Test
    public void changeSelectedCtrlTest() {
        utils.changeSelection(2L);
        assertEquals(2L, utils.getSelectedCardId());
    }

    @Test
    public void activeCardListTest() {
        utils.setActiveCardListId(1L);

        assertEquals(sampleCardList, utils.getActiveCardList());
        assertEquals(sampleCardListCtrl, utils.getActiveCardListCtrl());
    }
}
