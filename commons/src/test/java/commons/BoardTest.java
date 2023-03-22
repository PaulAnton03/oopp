package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    void setgetCardLists() {
        Set<CardList> cards = new HashSet<>();
        board.setCardLists(cards);
        assertSame(board.getCardLists(), cards);
    }

    @Test
    void addCardListDefault() {
        assertTrue(board.addCardList());
        assertNotNull(board.getCardLists());
        assertEquals(board.getCardLists().size(), 1);
        assertTrue(board.getCardLists().stream().findFirst().get() instanceof CardList);
    }

    @Test
    void addCardList() {
        CardList list = new CardList();
        assertTrue(board.addCardList(list));
        assertFalse(board.addCardList(list));
        assertNotNull(board.getCardLists());
        assertEquals(board.getCardLists().size(), 1);
        assertSame(board.getCardLists().stream().findFirst().get(), list);
    }

    @Test
    void removeCardList() {
        CardList list = new CardList("Title");
        board.addCardList(list);
        assertFalse(board.removeCardList(new CardList()));
        assertTrue(board.removeCardList(list));
        assertEquals(board.getCardLists().size(), 0);
    }

    @Test
    void isNetworkValid() {
        board.setCardLists(null);
        assertFalse(board.isNetworkValid());

        board.setCardLists(new HashSet<>());
        assertTrue(board.isNetworkValid());

        board.setName("");
        assertFalse(board.isNetworkValid());
    }
}
