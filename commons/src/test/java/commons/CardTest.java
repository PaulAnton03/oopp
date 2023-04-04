package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    Card card = new Card("task", "description");

    @Test
    void hashCodeTest() {
        Card card2 = new Card("task", "description");
        assertEquals(card.hashCode(), card2.hashCode());

        card2.setTitle("task2");
        assertNotEquals(card.hashCode(), card2.hashCode());

        card2.setTitle("task");
        card2.setDescription("description2");
        assertNotEquals(card.hashCode(), card2.hashCode());

        card2.setDescription("description");
        card2.setCardList(new CardList());
        assertNotEquals(card.hashCode(), card2.hashCode());
    }

    @Test
    void equalsTest() {
        Card card2 = new Card("task", "description");
        assertTrue(card.equals(card2));
        assertTrue(card2.equals(card));

        card2.setTitle("task2");
        assertFalse(card.equals(card2));
        assertFalse(card2.equals(card));

        card2.setTitle("task");
        card2.setDescription("description2");
        assertFalse(card.equals(card2));
        assertFalse(card2.equals(card));

        card2.setDescription("description");
        card2.setCardList(new CardList());
        assertFalse(card.equals(card2));
        assertFalse(card2.equals(card));
    }

    @Test
    void toStringTest() {
        String cardOutput = card.toString();
        String expectedOutput = "Card [id=0, title=task, description=description, cardList=null]";
        assertEquals(expectedOutput, cardOutput);
    }

    @Test
    void SetGetCardList() {
        CardList list = new CardList();
        card.setCardList(list);
        assertSame(card.getCardList(), list);
    }

    @Test
    void getTitle() {
        assertEquals(card.getTitle(), "task");
    }

    @Test
    void setTitle() {
        card.setTitle("Title.");
        assertEquals(card.getTitle(), "Title.");

        card.setTitle("task");
    }

    @Test
    void getDescription() {
        assertEquals(card.getDescription(), "description");
    }

    @Test
    void setDescription() {
        card.setDescription("Description.");
        assertEquals(card.getDescription(), "Description.");
    }

    @Test
    void isNetworkValid() {
        assertTrue(card.isNetworkValid());

        card.setTitle("");
        assertFalse(card.isNetworkValid());

        card.setTitle("task");
        card.setDescription("");
        assertTrue(card.isNetworkValid());
    }
}
