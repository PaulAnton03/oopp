package commons;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class CardListTest {

    CardList cardList = new CardList("MyList");
    Card card = new Card("Do the dishes", "I need to do the dishes before I go out tonight");

    @Test
    void removeCard() {
        cardList.addCard(card);
        cardList.removeCard(card);
        assertTrue(cardList.getCardList().isEmpty());
    }

    @Test
    void addCard() {
        cardList.addCard(card);
        assertFalse(cardList.getCardList().isEmpty());
    }

    @Test
    void getCardList() {
        assertNotNull(cardList.getCardList());
    }


    @Test
    void getTitle() {
        assertEquals("MyList", cardList.getTitle());
    }

    @Test
    void setId() {
        cardList.setId(10);
        assertEquals(10, cardList.getId());
    }

    @Test
    void setCardList() {
        CardList a1 = new CardList();
        cardList.setCardList(a1.getCardList());
        assertSame(a1.getCardList(), cardList.getCardList());
    }

    @Test
    void setTitle() {
        cardList.setTitle("lol");
        assertEquals("lol", cardList.getTitle());
    }

    @Test
    void testEquals() {
        CardList cardList1 = new CardList("MyList");
        assertEquals(cardList1, cardList);
    }

}