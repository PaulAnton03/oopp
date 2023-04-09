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
        tag.getCardTags().add(card1);
        tag.getCardTags().add(card2);
        assertTrue(tag.getCardTags().contains(card1));
        assertTrue(tag.getCardTags().contains(card2));
    }


}