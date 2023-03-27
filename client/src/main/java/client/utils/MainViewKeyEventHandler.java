package client.utils;

import client.scenes.MainViewCtrl;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class MainViewKeyEventHandler implements EventHandler<KeyEvent> {
    private final MainViewCtrl mainViewCtrl;

    public MainViewKeyEventHandler(MainViewCtrl mainViewCtrl) {
        this.mainViewCtrl = mainViewCtrl;
    }

    public void handle(KeyEvent e) {
        mainViewCtrl.getBoardCtrl().handleKeyEvent(e);
    }
}
