package client.scenes;

import client.utils.ClientPreferences;
import client.utils.ClientUtils;
import client.utils.ServerUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import org.springframework.ui.context.Theme;

import javax.inject.Inject;

public class ThemeEditorCtrl implements SceneCtrl {

    private final ClientUtils client;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final ThemeUtils themeUtils;

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
    private ComboBox<?> cardFont;

    @FXML
    private ColorPicker listColor;

    @FXML
    private ComboBox<?> listFont;

    @FXML
    private ComboBox<?> presets;

    @FXML
    void applyChanges() {
        //todo-apply
        goBack();
    }

    @FXML
    void goBack() {

    }

}
