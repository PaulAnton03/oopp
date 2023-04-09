package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTagTest {

    @Test
    public void testSetters() {
        // Create test data
        Card card = new Card();
        Tag tag = new Tag();
        CardTag cardTag = new CardTag();

        // Test setting Card and Tag
        cardTag.setCard(card);
        cardTag.setTag(tag);

        // Assert that the setters worked correctly
        assertEquals(card, cardTag.getCard());
        assertEquals(tag, cardTag.getTag());
    }
    @Test
    public void testConstructorAndGetters() {
        // Create test data
        Card card = new Card();
        Tag tag = new Tag();

        // Create new instance of CardTag
        CardTag cardTag = new CardTag(card, tag);

        // Assert that the CardTag instance was created successfully
        assertNotNull(cardTag);
        assertNull(cardTag.getId());
        assertEquals(card, cardTag.getCard());
        assertEquals(tag, cardTag.getTag());
    }@Test
    public void testToString() {
        // Create test data
        Card card = new Card();
        card.setId(1L);
        card.setTitle("Test Card");
        Tag tag = new Tag();
        tag.setId(2L);
        tag.setText("Test Tag");
        CardTag cardTag = new CardTag(card, tag);

        // Test the toString() method
        String expected = "CardTag{id=null, card=Test Card card-id: 1, tag=Test Tag tag_id: 2}";
        assertEquals(expected, cardTag.toString());
    }
    @Test
    public void testEqualsAndHashCode() {
        // Create test data
        Card card1 = new Card();
        card1.setId(1L);
        card1.setTitle("Test Card 1");
        Tag tag1 = new Tag();
        tag1.setId(2L);
        tag1.setText("Test Tag 1");
        CardTag cardTag1 = new CardTag(card1, tag1);
        cardTag1.setId(3L);

        Card card2 = new Card();
        card2.setId(1L);
        card2.setTitle("Test Card 2");
        Tag tag2 = new Tag();
        tag2.setId(2L);
        tag2.setText("Test Tag 2");
        CardTag cardTag2 = new CardTag(card2, tag2);
        cardTag2.setId(3L);

        Card card3 = new Card();
        card3.setId(3L);
        card3.setTitle("Test Card 3");
        Tag tag3 = new Tag();
        tag3.setId(4L);
        tag3.setText("Test Tag 3");
        CardTag cardTag3 = new CardTag(card3, tag3);
        cardTag3.setId(1L);

        // Test equals() method
        assertEquals(cardTag1, cardTag2);
        assertEquals(cardTag2, cardTag1);
        assertEquals(cardTag1.hashCode(), cardTag2.hashCode());

        // Test not equals() method
        assertNotEquals(cardTag1, cardTag3);
        assertNotEquals(cardTag3, cardTag1);
        assertNotNull(cardTag1);
        assertNotEquals(cardTag1, new Object());
    }
}