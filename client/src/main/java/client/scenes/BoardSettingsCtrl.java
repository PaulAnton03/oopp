package client.scenes;

import client.utils.*;
import com.google.inject.Inject;

import commons.Board;
import commons.CardTag;
import commons.Tag;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;


public class BoardSettingsCtrl implements SceneCtrl {

    private final ServerUtils server;
    private final ClientUtils client;
    private final MainCtrl mainCtrl;

    private final ClientPreferences clientPrefs;

    @FXML
    private PasswordField boardPassword;
    @FXML
    private CheckBox passwordUsed;
    @FXML
    private TextField boardName;
    @FXML
    private TextField inviteKeyField;
    @FXML
    private AnchorPane inviteKeyFooter;


    @Inject
    public BoardSettingsCtrl(ServerUtils server, ClientUtils client, MainCtrl mainCtrl,
                             ExceptionHandler exceptionHandler, ClientPreferences clientPrefs) {
        this.client = client;
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.clientPrefs = clientPrefs;
    }

    public void loadData() {
        boardName.setText(client.getBoardCtrl().getBoard().getName());

        Board board = client.getBoardCtrl().getBoard();
        // The key format is: "join-talio#<server address>#<board id>(#<password>)"
        String inviteKey = "join-talio#" + server.getServerPath() + "#" + board.getId();
        if (board.getPassword() != null) {
            inviteKey += "#" + board.getPassword();
        }
        inviteKeyField.setText(inviteKey);
    }

    public void saveChanges() {
        if(boardName.getText().isEmpty()) {
            throw new IllegalStateException("Board must have a name");
        }
        Board board = client.getBoardCtrl().getBoard();
        if (board == null) {
            Logger.log("No board selected", Logger.LogLevel.ERROR);
            throw new IllegalStateException("Something went wrong, no board selected!");
        }
        board.setName(boardName.getText());
        board.setPassword(passwordUsed.isSelected() ? boardPassword.getText() : null);
        server.updateBoard(board);
        clearForm();
        mainCtrl.showMainView();
    }

    public void deleteBoard() {
        Board board = client.getBoardCtrl().getBoard();
        if (board == null) {
            Logger.log("No board selected", Logger.LogLevel.ERROR);
            throw new IllegalStateException("Something went wrong, no board selected!");
        }
        //this for loop is for deleting cardTags if a tag is in this board.
        for(CardTag cardTag : server.getCardTags()){
            for(Tag tag : board.getTagList()){
                if(tag.equals(cardTag.getTag())){
                    server.deleteCardTag(cardTag.getId());
                }
            }
        }
        clientPrefs.removeJoinedBoard(board.getId());
        server.deleteBoard(board.getId());
        client.getBoardCtrl().remove();
        Logger.log("Deleted board " + board);
        mainCtrl.showJoin();
    }

    public void goBack() {
        clearForm();
        mainCtrl.showMainView();
    }

    public void clearForm() {
        boardName.setText(client.getBoardCtrl().getBoard().getName());
        boardPassword.setText("");
        passwordUsed.setSelected(false);
    }

    public void copyInviteKey() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(inviteKeyField.getText());
        clipboard.setContent(content);
        Alert inviteKeyCopied = new Alert(Alert.AlertType.CONFIRMATION);
        inviteKeyCopied.setContentText("Invite key copied!");
        inviteKeyCopied.show();
    }

    @Override
    public void revalidate() {

    }

    @FXML
    public void editTheme() {
        clearForm();
        mainCtrl.showThemeEditor();
    }
}
