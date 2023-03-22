package client.utils;

import javax.inject.Inject;

import java.util.Optional;
import java.util.prefs.Preferences;

/**
 * Class designated to accessing and modifying client-side data,
 * like the default board and passwords for joined boards.
 */
public class ClientPreferences {
    private final Preferences commonPrefs;
    private final Preferences savedPasswords;
    private final static String DEFAULT_BOARD_KEY = "default_board";
    private final static String SAVED_PASSWORDS_PATH = "/passwords";

    @Inject
    public ClientPreferences(Preferences preferences) {
        this.commonPrefs = preferences;
        this.savedPasswords = preferences.node(SAVED_PASSWORDS_PATH);
    }

    /**
     * Get the id of the default board to show
     * @return optional of the id, empty optional if not set
     */
    public Optional<Long> getDefaultBoardId() {
        long boardId = commonPrefs.getLong(DEFAULT_BOARD_KEY, -1);
        return boardId == -1 ? Optional.empty() : Optional.of(boardId);
    }

    /**
     * Set the id of the default board to show
     * @throws IllegalArgumentException if supplied id is a negative number
     * @param boardId the id of the board
     */
    public void setDefaultBoardId(long boardId) {
        validateBoardId(boardId);
        commonPrefs.putLong(DEFAULT_BOARD_KEY, boardId);
    }

    public void unsetDefaultBoardId() {
        commonPrefs.remove(DEFAULT_BOARD_KEY);
    }

    /**
     * Get a saved password for a specified board
     * @param boardId the id of the board
     * @throws IllegalArgumentException if supplied id is a negative number
     * @return optional of the password, empty optional if not set
     */
    public Optional<String> getPasswordForBoard(long boardId) {
        validateBoardId(boardId);
        String password = savedPasswords.get(String.valueOf(boardId), null);
        return password == null ? Optional.empty() : Optional.of(password);
    }

    /**
     * Store a password for a board
     * @throws IllegalArgumentException if supplied id is a negative number
     * @param boardId the id of the board
     * @param password the password to be stored
     */
    public void setPasswordForBoard(long boardId, String password) {
        validateBoardId(boardId);
        savedPasswords.put(String.valueOf(boardId), password);
    }

    public void unsetPasswordForBoard(long boardId) {
        validateBoardId(boardId);
        savedPasswords.remove(String.valueOf(boardId));
    }

    public void validateBoardId(long boardId) {
        if (boardId < 0) {
            throw new IllegalArgumentException(boardId + " is invalid id for a board");
        }
    }
}
