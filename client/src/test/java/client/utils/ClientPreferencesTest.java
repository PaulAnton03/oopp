package client.utils;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.prefs.Preferences;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class ClientPreferencesTest {
    private ClientPreferences clientPreferences;

    @BeforeAll
    public void setUp() {
        Preferences testPreferences = Preferences.userRoot().node("/test");
        clientPreferences = new ClientPreferences(testPreferences);
    }

    @Test
    public void testBoardSave() {
        clientPreferences.setDefaultBoardId(27);
        Optional<Long> boardId = clientPreferences.getDefaultBoardId();

        assertTrue(boardId.isPresent());
        assertEquals(27, boardId.get());
    }

    @Test
    public void testPasswordSave() {
        clientPreferences.setPasswordForBoard(27, "Password123");
        Optional<String> password = clientPreferences.getPasswordForBoard(27);

        assertTrue(password.isPresent());
        assertEquals("Password123", password.get());
    }

    @Test
    public void testEmptySettings() {
        clientPreferences.unsetDefaultBoardId();
        clientPreferences.unsetPasswordForBoard(27);

        // TODO: uncomment this when there is no longer a default value in getDefaultBoardId()
        // assertTrue(clientPreferences.getDefaultBoardId().isEmpty());
        assertTrue(clientPreferences.getPasswordForBoard(27).isEmpty());
    }

    @AfterAll
    public void destroy() {
        try {
            Preferences.userRoot().node("/test").clear();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
