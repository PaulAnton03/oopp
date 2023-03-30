package commons;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {
    SubTask subTask = new SubTask("subtask", true);

    @Test
    void SetGetCard() {
        Card card = new Card();
        subTask.setCard(card);
        assertSame(subTask.getCard(), card);
    }

    @Test
    void getTitle() {
        assertEquals("subtask", subTask.getTitle());
    }

    @Test
    void getFinished() {
        assertEquals(true, subTask.getFinished());
    }

    @Test
    void setTitle() {
        subTask.setTitle("New");
        assertEquals("New", subTask.getTitle());
    }

    @Test
    void setFinished() {
        subTask.setFinished(false);
        assertEquals(false, subTask.getFinished());
    }

    @Test
    void isNetworkValid() {
        assertTrue(subTask.isNetworkValid());

        subTask.setTitle("");
        assertFalse(subTask.isNetworkValid());

        subTask.setTitle("subtask");
        subTask.setFinished(false);
        assertTrue(subTask.isNetworkValid());
    }
}