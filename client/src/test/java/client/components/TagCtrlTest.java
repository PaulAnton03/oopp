package client.components;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TagCtrlTest {

    @Test
    void loadData() {
        Color color;
        color = Color.rgb(100, 100,100);
        String string = "-fx-background-color: #" + color;
        string = string.replaceAll("0x", "");
        System.out.println(string);
    }

    @Test
    void getNode() {
    }

    @Test
    void editTag() {
    }

    @Test
    void assignTagToCard() {
    }

    @Test
    void delete() {
    }

    @Test
    void refresh() {
    }

    @Test
    void remove() {
    }

    @Test
    void removeChildren() {
    }

    @Test
    void replaceChild() {
    }

    @Test
    void getTag() {
    }
}