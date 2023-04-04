package client.scenes;

import client.utils.ClientUtils;
import client.utils.ServerUtils;
import client.utils.ThemeUtils;
import commons.Board;
import commons.Card;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;

import javax.inject.Inject;
import java.util.stream.Collectors;

public class ThemeEditorCtrl implements SceneCtrl {

    private final ClientUtils client;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final ThemeUtils themeUtils;
    private Board board;
    private CardList cardList;
    private Card card;

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
    private ComboBox<?> boardFont;

    @FXML
    private ColorPicker cardColor;

    @FXML
    private ColorPicker listColor;

    @FXML
    private ComboBox<?> presets;

    public void loadData() {
        List<Theme> loadPresets = themeUtils.getPredefinedThemes();
        presets.getItems().addAll(loadPresets.stream().map(c -> c.toString()).collect(Collectors.toList()));
    }

    @FXML
    void applyChanges() {
        //todo-apply
        goBack();
    }

    @FXML
    void goBack() {
        mainCtrl.showMainView();
    }

}
