package client.scenes;

import client.components.BoardCtrl;
import client.utils.*;
import com.google.inject.Inject;
import commons.Board;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.stream.Collectors;

public class CreateBoardCtrl implements SceneCtrl {

    private final ServerUtils server;
    private final ClientUtils client;
    private final MainCtrl mainCtrl;
    private final ComponentFactory factory;
    private final ClientPreferences clientPrefs;
    private final ThemeUtils themeUtils;

    @FXML
    private TextField boardName;
    @FXML
    private TextField boardPassword;
    @FXML
    private CheckBox passwordUsed;
    @FXML
    private ComboBox<String> themePicker;

    @Inject
    public CreateBoardCtrl(ServerUtils server, ClientUtils client, MainCtrl mainCtrl,
                           ComponentFactory factory, ClientPreferences clientPrefs, ThemeUtils themeUtils) {
        this.client = client;
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.factory = factory;
        this.clientPrefs = clientPrefs;
        this.themeUtils = themeUtils;
    }

    public void loadData() {
        themePicker.setValue("");
        themePicker.getItems().clear();
        List<ThemeUtils.Theme> predefined = ThemeUtils.Theme.getPredefinedThemes();
        themePicker.getItems().addAll(predefined.stream().map(ThemeUtils.Theme::toString).collect(Collectors.toList()));
    }

    public void loadBoard(Board board, ThemeUtils.Theme theme) {
        board.setBoardColor(theme.getBoardColor());
        board.setListColor(theme.getListColor());
        board.setCardColor(theme.getCardColor());
        board.setFont(null);
    }

    public void createBoard() {
        final Board newBoard = new Board(boardName.getText());
        if (passwordUsed.isSelected()) {
            newBoard.setPassword(boardPassword.getText());
        }
        ThemeUtils.Theme theme = ThemeUtils.Theme.valueOf(themePicker.getValue());
        loadBoard(newBoard, theme);
        final Board addedBoard = server.addBoard(newBoard);
        Logger.log("Added board " + addedBoard);

        final CardList cardList = new CardList("TODO");
        cardList.setBoard(addedBoard);
        final CardList addedCardList = server.addCardList(cardList);
        addedBoard.addCardList(addedCardList);

        clear();
        BoardCtrl boardCtrl = factory.create(BoardCtrl.class, addedBoard);
        client.setBoardCtrl(boardCtrl);
        clientPrefs.setDefaultBoardId(addedBoard.getId());
        clientPrefs.addJoinedBoard(addedBoard.getId());
        mainCtrl.showMainView();
    }

    public void goBack() {
        clear();
        mainCtrl.showMainView();
    }

    public void clear() {
        boardName.setText("");
        boardPassword.setText("");
    }
}
