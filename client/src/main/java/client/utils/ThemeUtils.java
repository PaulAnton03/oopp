package client.utils;

import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

@Data
public class ThemeUtils {

    private ClientUtils clientUtils;

    @Inject
    public ThemeUtils(ClientUtils clientUtils) {
        this.clientUtils = clientUtils;
    }

    private Font font;

    public void updateFont() {
        if (font == null) return;
        updateStyle(clientUtils.getBoardCtrl().getNode());
        clientUtils.getCardListCtrls().values().forEach(c -> updateStyle(c.getNode()));
        clientUtils.getCardCtrls().values().forEach(c -> updateStyle(c.getNode());
    }

    private void updateStyle(Parent node) {
        String style = node.getStyle();
        boolean containsFont = style.contains("-fx-font-family");
        if (!containsFont) style += "-fx-font-family: " + font.getName() + ";";
        else {
            int start = style.indexOf("-fx-font-family");
            int end = style.indexOf(";", start);
            style = style.substring(0, start) + "-fx-font-family: " + font.getName() + style.substring(end);
        }
    }

    public String turnColorIntoString(Color color) {
        return color.toString().replace("0x", "#");
    }

    @Data
    @AllArgsConstructor
    public class Theme {
        private String name;
        private String boardColor;
        private String cardColor;
        private String listColor;
        private Font font;

        public String toString() {
            return name;
        }
    }

    public List<Theme> getPredefinedThemes() {
        return Arrays.asList(
                new Theme("Default", "#ffffffff", "#ffffffff", "#b2b2ebff", Font.font("Arial")),
                new Theme("Dark", "#000000ff", "#000000ff", "#000000ff", Font.font("Arial")));
    }
}
