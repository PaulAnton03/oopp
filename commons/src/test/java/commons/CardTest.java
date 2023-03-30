package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    Card card = new Card("task", "description");

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
