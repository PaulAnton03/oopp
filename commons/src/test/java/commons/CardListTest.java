package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardListTest {

    CardList cardList = new CardList("MyList");
    Card card = new Card("Do the dishes", "I need to do the dishes before I go out tonight");

    @Test
    void hashCodeTest() {
        CardList cardList1 = new CardList("MyList");
        assertEquals(cardList1.hashCode(), cardList.hashCode());
        cardList1.setTitle("MyList1");
        assertNotEquals(cardList1.hashCode(), cardList.hashCode());
        cardList1.setTitle("MyList");
        cardList1.addCard(card);
        assertNotEquals(cardList1.hashCode(), cardList.hashCode());
    }

    @Test
    void getBoard() {
        assertNull(cardList.getBoard());
    }

    @Test
    void constructorTest() {
        CardList cardList1 = new CardList("MyList");
        assertNotNull(cardList1);
        assertEquals(cardList1.getTitle(), "MyList");
        assertNotNull(cardList1.getCards());

        cardList1 = new CardList("MyList", "color");
        assertNotNull(cardList1);
        assertEquals(cardList1.getTitle(), "MyList");
        assertNotNull(cardList1.getCards());
    }

    @Test
    void setColor() {
        cardList.setColor("color");
        assertEquals("color", cardList.getColor());

        cardList = new CardList("MyList");
    }

    @Test
    void toStringTest() {
        String cardListOutput = cardList.toString();
        String expectedOutput = "CardList [id=0, title=MyList, color=#b2b2ebff, cards=[]]";
        assertEquals(expectedOutput, cardListOutput);
    }

    @Test
    void removeCard() {
        cardList.addCard(card);
        cardList.removeCard(card);
        assertTrue(cardList.getCards().isEmpty());

        cardList.addCard(card);
        cardList.removeCard(0);
        assertTrue(cardList.getCards().isEmpty());
    }

    @Test
    void addCard() {
        cardList.addCard(card);
        assertFalse(cardList.getCards().isEmpty());

        Card c = new Card();
        cardList.addCardAtPosition(c, 0);
        assertSame(c, cardList.getCards().get(0));
    }

    @Test
    void getCardList() {
        assertNotNull(cardList.getCards());
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
        cardList.setCards(a1.getCards());
        assertSame(a1.getCards(), cardList.getCards());
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

        cardList1.setTitle("MyList1");
        assertNotEquals(cardList1, cardList);

        cardList1.setTitle("MyList");
        cardList1.addCard(card);
        assertNotEquals(cardList1, cardList);
    }

}