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
    void setgetCardLists() {
        List<CardList> cards = new ArrayList<>();
        board.setCardLists(cards);
        assertSame(board.getCardLists(), cards);
    }

    @Test
    void addCardListDefault() {
        assertTrue(board.addCardList());
        assertNotNull(board.getCardLists());
        assertEquals(board.getCardLists().size(), 1);
        assertTrue(board.getCardLists().get(0) instanceof CardList);
    }

    @Test
    void addCardList() {
        CardList list = new CardList();
        assertTrue(board.addCardList(list));
        assertFalse(board.addCardList(list));
        assertNotNull(board.getCardLists());
        assertEquals(board.getCardLists().size(), 1);
        assertSame(board.getCardLists().get(0), list);
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

        board.setCardLists(new ArrayList<CardList>());
        assertTrue(board.isNetworkValid());

        board.setName("");
        assertFalse(board.isNetworkValid());
    }
}
