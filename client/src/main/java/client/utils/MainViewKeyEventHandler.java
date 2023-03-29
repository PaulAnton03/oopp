package client.utils;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public class MainViewKeyEventHandler implements EventHandler<KeyEvent> {
    private final ClientUtils client;

    public MainViewKeyEventHandler(ClientUtils client) {
        this.client = client;
    }

    public void handle(KeyEvent e) {
        client.getBoardCtrl().handleKeyEvent(e);
    }
}
