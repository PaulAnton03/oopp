package commons;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TagTest {

    Tag tag = new Tag();
    Board board = new Board("Test Board");




    @Test
    void getId() {
        Tag tag = new Tag();
        tag.setId(1);
        assertEquals(1, tag.getId());
    }

    @Test
    void getText() {
        Tag tag = new Tag();
        tag.setText("test tag");
        assertEquals("test tag", tag.getText());
    }

    @Test
    void setId() {
        Tag tag = new Tag();
        tag.setId(10);
        assertEquals(10, tag.getId());
    }


    @Test
    void setText() {
        Tag tag = new Tag();
        tag.setText("test tag");
        assertEquals("test tag", tag.getText());
    }

    @Test
    void testGettersAndSetters() {
        tag.setBoard(board);
        tag.setText("Test Tag");
        tag.setColor("00000000");
        assertEquals(tag.getBoard(), board);
        assertEquals("Test Tag", tag.getText());
        assertEquals("00000000", tag.getColor());

    }

    @Test
    void testSetAndGetColor() {
        Tag tag = new Tag();
        tag.setColor("12345678");
        assertEquals("12345678", tag.getColor());
    }





}