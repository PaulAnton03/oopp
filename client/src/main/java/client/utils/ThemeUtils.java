package client.utils;

import javafx.scene.Parent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lombok.Data;

import javax.inject.Inject;

@Data
public class ThemeUtils {

    private ClientUtils clientUtils;

    @Inject
    public ThemeUtils(ClientUtils clientUtils) {
        this.clientUtils = clientUtils;
    }

    private Font boardFont;
    private Font listFont;
    private Font cardFont;

    public void updateFont() {
        if (boardFont != null)
            updateStyle(clientUtils.getBoardCtrl().getNode(), boardFont);
        if (listFont != null)
            clientUtils.getCardListCtrls().values().forEach(c -> updateStyle(c.getNode(), listFont));
        if (cardFont != null)
            clientUtils.getCardCtrls().values().forEach(c -> updateStyle(c.getNode(), cardFont));
    }

    private void updateStyle(Parent node, Font font) {
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
}
