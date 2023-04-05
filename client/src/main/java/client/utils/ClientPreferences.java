package client.utils;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;

/**
 * Class designated to accessing and modifying client-side data,
 * like the default board and passwords for joined boards.
 */
public class ClientPreferences {
    private final Preferences commonPrefs;
    private final Preferences savedPasswords;
    private final Preferences savedBoards;
    private final static String DEFAULT_BOARD_KEY = "default_board";
    private final static String SAVED_BOARDS_PATH = "boards";
    private final static String SAVED_PASSWORDS_PATH = "passwords";

    @Inject
    public ClientPreferences(ServerUtils serverUtils, Preferences preferences) {
        // The directory for a server is its path hashcode so there aren't any illegal characters
        Preferences serverSpecificPrefs = preferences.node(String.valueOf(serverUtils.getServerPath().hashCode()));
        this.commonPrefs = serverSpecificPrefs;
        this.savedPasswords = serverSpecificPrefs.node(SAVED_PASSWORDS_PATH);
        this.savedBoards = serverSpecificPrefs.node(SAVED_BOARDS_PATH);
    }

    /**
     * Get the id of the default board to show
     *
     * @return optional of the id, empty optional if not set
     */
    public Optional<Long> getDefaultBoardId() {
        long boardId = commonPrefs.getLong(DEFAULT_BOARD_KEY, -1);
        return boardId == -1 ? Optional.empty() : Optional.of(boardId);
    }

    /**
     * Set the id of the default board to show
     *
     * @param boardId the id of the board
     * @throws IllegalArgumentException if supplied id is a negative number
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
     *
     * @param boardId the id of the board
     * @return optional of the password, empty optional if not set
     * @throws IllegalArgumentException if supplied id is a negative number
     */
    public Optional<String> getPasswordForBoard(long boardId) {
        validateBoardId(boardId);
        String password = savedPasswords.get(String.valueOf(boardId), null);
        return password == null ? Optional.empty() : Optional.of(password);
    }

    public void clearPreferences() {
        try {
            commonPrefs.clear();
            savedPasswords.clear();
            savedBoards.clear();
        } catch (BackingStoreException e) {
            throw new RuntimeException(e);
        }

    }
    public List<Long> getJoinedBoards() {
        try {
            return List.of(savedBoards.keys()).stream().map(c -> Long.parseLong(c)).collect(Collectors.toList());
        } catch (BackingStoreException e) {
            return new ArrayList<>();
        }
    }

    public void addJoinedBoard(long boardId) {
        savedBoards.put(String.valueOf(boardId), "");
    }

    public void removeJoinedBoard(long boardId) {
        savedBoards.remove(String.valueOf(boardId));
        savedPasswords.remove(String.valueOf(boardId));
        commonPrefs.remove(DEFAULT_BOARD_KEY);
    }

    /**
     * Store a password for a board
     *
     * @param boardId  the id of the board
     * @param password the password to be stored
     * @throws IllegalArgumentException if supplied id is a negative number
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
