package commons;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CardListTest {

    CardList cardList = new CardList();
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
    void getId() {
        assertEquals(0, card.getId());
        //id of the card is 0
    }

    @Test
    void getCardList() {
        assertNotNull(cardList.getCardList());
    }

    @Test
    void getBoard() {
        //TODO test this method
    }

    @Test
    void getTitle() {
        assertEquals("New Card List", cardList.getTitle());
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
    }


    @Test
    void setTitle() {
        cardList.setTitle("lol");
        assertEquals("lol", cardList.getTitle());
    }

    @Test
    void testEquals() {
        CardList cardList1 = new CardList();
        assertEquals(cardList1, cardList);
    }


    @Test
    void testHashCode() {
        CardList cardList1 = new CardList();
        assertEquals(cardList.hashCode(), cardList1.hashCode());
    }

    @Test
    void testToString() {
        assertEquals("CardList(id=0, cardList=[], board=null, title=New Card List)", cardList.toString());
    }
}