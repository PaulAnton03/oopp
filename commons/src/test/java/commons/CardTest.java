package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    Card card = new Card("task", "description");

    @Test
    void setgetCardList() {
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
    void isValid() {
        card.setCardList(null);
        assertFalse(card.isValid());

        card.setCardList(new CardList());
        assertTrue(card.isValid());

        card.setTitle("");
        assertFalse(card.isValid());

        card.setTitle("task");
        card.setDescription("");
        assertFalse(card.isValid());
    }
}
