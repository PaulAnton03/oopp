package commons;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    Board board = new Board("title");

    @Test
    void getName() {
        assertEquals(board.getName(), "title");
    }

    @Test
    void toStringTest() {
        String boardOutput = board.toString();
        String expectedOutput = "Board [id=0, name=title, password=null, cardLists=[], tags=[]]";
        assertEquals(expectedOutput, boardOutput);
    }

    @Test
    void hashCodeTest() {
        Board board2 = new Board("title");
        assertEquals(board.hashCode(), board2.hashCode());

        board2.setName("title2");
        assertNotEquals(board.hashCode(), board2.hashCode());

        board2.setName("title");
        board2.setPassword("password");
        assertNotEquals(board.hashCode(), board2.hashCode());

        board2.setPassword(null);
        assertEquals(board.hashCode(), board2.hashCode());

        board2.addCardList();
        assertNotEquals(board.hashCode(), board2.hashCode());
    }

    @Test
    void constructorTest() {
        Board board2 = new Board("title");
        assertNotNull(board2);
        assertEquals(board2.getName(), "title");
        assertNull(board2.getPassword());

        board2 = new Board("title");
        assertNotNull(board2);
        assertEquals(board2.getName(), "title");
        assertNull(board2.getPassword());
    }

    @Test
    void setEditable() {
        board.setEditable(true);
        assertTrue(board.isEditable());
        board.setEditable(false);
        assertFalse(board.isEditable());
    }

    @Test
    void equalsTest() {
        Board board2 = new Board("title");
        assertEquals(board, board2);

        board2.setName("title2");
        assertNotEquals(board, board2);

        board2.setName("title");
        board2.setPassword("password");
        assertNotEquals(board, board2);

        board2.setPassword(null);
        assertEquals(board, board2);

        board2.addCardList();
        assertNotEquals(board, board2);
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

        board.setCardLists(new ArrayList<>());
        assertTrue(board.isNetworkValid());

        board.setName("");
        assertFalse(board.isNetworkValid());
    }
}
