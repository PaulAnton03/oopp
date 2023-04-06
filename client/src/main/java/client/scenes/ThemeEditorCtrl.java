package client.scenes;

import client.utils.ClientUtils;
import client.utils.ServerUtils;
import client.utils.ThemeUtils;
import commons.Board;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.paint.Color;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

public class ThemeEditorCtrl implements SceneCtrl {

    private final ClientUtils client;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final ThemeUtils themeUtils;
    private Board board;

    @Inject
    public ThemeEditorCtrl(ClientUtils client, ServerUtils server,
                           MainCtrl mainCtrl, ThemeUtils themeUtils) {
        this.client = client;
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.themeUtils = themeUtils;
    }

    @FXML
    private ColorPicker boardColor;

    @FXML
    private ColorPicker fontColor;

    @FXML
    private ColorPicker cardColor;

    @FXML
    private ColorPicker listColor;

    @FXML
    private ComboBox<String> presets;

    public void loadData() {
        presets.setValue(null);
        board = client.getBoardCtrl().getBoard();
        presets.getItems().clear();
        List<ThemeUtils.Theme> loadPresets = ThemeUtils.Theme.getPredefinedThemes();
        presets.getItems().addAll(loadPresets.stream().map(ThemeUtils.Theme::toString).collect(Collectors.toList()));
        loadDefaultValues(board.getBoardColor(), board.getListColor(), board.getCardColor(), board.getFontColor());
    }

    private void loadDefaultValues(String boardColor, String listColor, String cardColor, String fontColor) {
        this.boardColor.setValue(Color.valueOf(boardColor));
        this.listColor.setValue(Color.valueOf(listColor));
        this.cardColor.setValue(Color.valueOf(cardColor));
        this.fontColor.setValue(Color.valueOf(fontColor));
    }

    @FXML
    void applyChanges() {
        setColors();
        server.updateBoard(board);
        goBack();
    }

    void setColors() {
        board.setBoardColor(themeUtils.turnColorIntoString(this.boardColor.getValue()));
        board.setListColor(themeUtils.turnColorIntoString(this.listColor.getValue()));
        board.setCardColor(themeUtils.turnColorIntoString(this.cardColor.getValue()));
        board.setFontColor(themeUtils.turnColorIntoString(this.fontColor.getValue()));
    }

    @FXML
    void goBack() {
        mainCtrl.showMainView();
    }

    @FXML
    public void loadPreset(ActionEvent event) {
        try {
            String selected = presets.getValue();
            var theme = ThemeUtils.Theme.valueOf(selected);
            loadDefaultValues(theme.getBoardColor(), theme.getListColor(), theme.getCardColor(), theme.getFontColor());
        } catch (Exception ignored) {}
    }

}
