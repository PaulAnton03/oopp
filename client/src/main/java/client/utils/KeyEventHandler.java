package client.utils;

import client.scenes.MainCtrl;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyEventHandler implements EventHandler<KeyEvent> {
    private final ClientUtils client;
    private final MainCtrl mainCtrl;

    public KeyEventHandler(ClientUtils client, MainCtrl mainCtrl) {
        this.client = client;
        this.mainCtrl = mainCtrl;
    }

    private static void displayKeybinds() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setHeaderText("Keyboard Shortcuts Help");
        alert.setContentText(
            "Arrow keys: Change selected card"
            + "\nShift + Arrow keys: Shift selected card up/down"
            + "\nEnter : Edit selected card"
            + "\nBackspace/Del : Delete selected card"
            + "\nEsc : Close task details menu"
            + "\n? : Show this menu"
        );
        alert.showAndWait();
    }

    public void handle(KeyEvent e) {
        if (client.getCardCtrl(client.getSelectedCardId()) != null
            && client.getEditedCardTitle() != null)
            return;
        switch (e.getCode()) {
            case LEFT:
            case RIGHT:
            case UP:
            case DOWN:
            case ENTER:
            case BACK_SPACE:
            case DELETE:
                if (mainCtrl.getActiveCtrl() == mainCtrl.getMainViewCtrl()) {
                    client.getBoardCtrl().handleKeyEvent(e);
                }
                break;
            case SLASH:
                if (e.isShiftDown()) {
                    displayKeybinds();
                }
                break;
            case ESCAPE:
                if (mainCtrl.getActiveCtrl() == mainCtrl.getEditCardCtrl()) {
                    mainCtrl.getEditCardCtrl().cancel();
                }
                break;
            case E:
                if (mainCtrl.getActiveCtrl() == mainCtrl.getMainViewCtrl()
                    && client.getCardCtrl(client.getSelectedCardId()) != null) {
                    e.consume();
                    client.getCardCtrl(client.getSelectedCardId()).editTitle();
                }
            default:
                break;
        }
    }
}
