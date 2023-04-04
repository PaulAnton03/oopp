package client.utils;

import client.scenes.MainCtrl;
import javafx.scene.paint.Color;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

@Data
public class ThemeUtils {

    private MainCtrl mainCtrl;

    @Inject
    public ThemeUtils(MainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
    }

    @Data
    @AllArgsConstructor
    public static class Theme {
        private String name;
        private String boardColor;
        private String cardColor;
        private String listColor;
        private String fontColor;

        public String toString() {
            return name;
        }

        public static List<Theme> getPredefinedThemes() {
            return Arrays.asList(
                    new Theme("Default", "#ffffffff", "#ffffffff", "#b2b2ebff", "#000000ff"),
                    new Theme("Dark", "#302c2cff", "#857676ff", "#423838ff", "#000000ff"),
                    new Theme("Cherry", "#ff867dff", "#f76157ff", "#000000ff", "#000000ff"),
                    new Theme("Space", "#84618fff", "#684373ff", "#b591bfff", "#000000ff"));
        }

        public static Theme valueOf(String theme) {
            return getPredefinedThemes().stream().filter(t -> t.getName().equals(theme)).findFirst().orElse(null);
        }
    }

    public String turnColorIntoString(Color color) {
        return color.toString().replace("0x", "#");
    }


}
