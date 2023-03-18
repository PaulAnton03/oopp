package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardTest {
    Card card = new Card("Do the dishes", "I need to do the dishes before I go out tonight");

    @Test
    void getId() {
        assertEquals(0, card.getId());
        //id of the card is 0
    }

}
