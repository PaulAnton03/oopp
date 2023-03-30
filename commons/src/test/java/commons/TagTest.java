package commons;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TagTest {

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
    void getCards() {
        Tag tag = new Tag();
        Card card1 = new Card();
        Card card2 = new Card();
        tag.getCards().add(card1);
        tag.getCards().add(card2);
        assertTrue(tag.getCards().contains(card1));
        assertTrue(tag.getCards().contains(card2));
    }

    @Test
    void getRed() {
        Tag tag = new Tag();
        tag.setRed(10);
        assertEquals(10, tag.getRed());
    }

    @Test
    void getBlue() {
        Tag tag = new Tag();
        tag.setBlue(102);
        assertEquals(102, tag.getBlue());
    }

    @Test
    void getGreen() {
        Tag tag = new Tag();
        tag.setGreen(1);
        assertEquals(1, tag.getGreen());
    }

    @Test
    void setRed() {
        Tag tag = new Tag();
        tag.setRed(1);
        assertEquals(1, tag.getRed());
    }

    @Test
    void setBlue() {
        Tag tag = new Tag();
        tag.setBlue(1);
        assertEquals(1, tag.getBlue());
    }

    @Test
    void setGreen() {
        Tag tag = new Tag();
        tag.setGreen(1);
        assertEquals(1, tag.getGreen());
    }

    @Test
    void colorTest(){
        Tag tag = new Tag();
        tag.setRed(255);
        tag.setGreen(0);
        tag.setBlue(0);
        assertEquals(255, tag.getRed());
        assertEquals(0, tag.getGreen());
        assertEquals(0, tag.getBlue());
    }
}