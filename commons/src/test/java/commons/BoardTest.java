package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class BoardTest {
    Board board = new Board("title");

    @Test
    void getName() {
        assertEquals(board.getName(), "title");
    }

    @Test
    void setName() {
        board.setName("Title.");
        assertEquals(board.getName(), "Title.");
    }

    @Test
    void getPassword() {
        assertNull(board.getPassword());
    }

    @Test
    void setPassword() {
        board.setPassword("Password.");
        assertEquals(board.getPassword(), "Password.");
    }

    @Test
    void setgetCards() {
        List<CardList> cards = new ArrayList<>();
        board.setCards(cards);
        assertSame(board.getCards(), cards);
    }

    @Test
    void addCardListDefault() {
        assertTrue(board.addCardList());
        assertNotNull(board.getCards());
        assertEquals(board.getCards().size(), 1);
        assertTrue(board.getCards().get(0) instanceof CardList);
    }

    @Test
    void addCardList() {
        CardList list = new CardList();
        assertTrue(board.addCardList(list));
        assertFalse(board.addCardList(list));
        assertNotNull(board.getCards());
        assertEquals(board.getCards().size(), 1);
        assertSame(board.getCards().get(0), list);
    }

    @Test
    void removeCardList() {
        CardList list = new CardList("Title");
        board.addCardList(list);
        assertFalse(board.removeCardList(new CardList()));
        assertTrue(board.removeCardList(list));
        assertEquals(board.getCards().size(), 0);
    }

    @Test
    void isValid() {
        board.setCards(null);
        assertFalse(board.isValid());

        board.setCards(new ArrayList<CardList>());
        assertTrue(board.isValid());

        board.setName("");
        assertFalse(board.isValid());
    }
}
