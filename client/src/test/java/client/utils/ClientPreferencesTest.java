package client.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.prefs.Preferences;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientPreferencesTest {
    private ClientPreferences clientPreferences;
    @Mock
    private ServerUtils serverUtils;

    @BeforeEach
    public void setUp() {
        Preferences testPreferences = Preferences.userRoot().node("/test");
        when(serverUtils.getServerPath()).thenReturn("https://this/is/test/path:8080");
        clientPreferences = new ClientPreferences(serverUtils, testPreferences);
    }

    @Test
    public void testBoardSave() {
        clientPreferences.setDefaultBoardId(27);
        Optional<Long> boardId = clientPreferences.getDefaultBoardId();

        assertTrue(boardId.isPresent());
        assertEquals(27, boardId.get());
    }

    @Test
    public void testJoinedBoards() {
        List<Long> boardIds = List.of(12L, 24L);
        clientPreferences.addJoinedBoard(boardIds.get(0));
        clientPreferences.addJoinedBoard(boardIds.get(1));

        assertEquals(boardIds, clientPreferences.getJoinedBoards());
    }

    @Test
    public void testRemovalFromJoinedBoards() {
        clientPreferences.addJoinedBoard(48L);
        clientPreferences.removeJoinedBoard(48L);

        assertDoesNotThrow(() -> clientPreferences.removeJoinedBoard(48L));
        assertEquals(List.of(), clientPreferences.getJoinedBoards());
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
        clientPreferences.setDefaultBoardId(23);
        clientPreferences.setPasswordForBoard(27, "password");
        clientPreferences.addJoinedBoard(23);
        clientPreferences.clearPreferences();

        assertTrue(clientPreferences.getDefaultBoardId().isEmpty());
        assertTrue(clientPreferences.getPasswordForBoard(27).isEmpty());
        assertTrue(clientPreferences.getJoinedBoards().isEmpty());
    }

    @AfterEach
    public void destroy() {
        try {
            Preferences.userRoot().node("/test").removeNode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
