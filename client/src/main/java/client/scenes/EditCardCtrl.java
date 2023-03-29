package client.scenes;

import javax.inject.Inject;

import client.utils.ClientUtils;
import client.utils.ExceptionHandler;
import client.utils.ServerUtils;
import commons.Card;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class EditCardCtrl implements SceneCtrl {
    private final ServerUtils server;
    private final ClientUtils client;
    private final MainCtrl mainCtrl;

    private final ExceptionHandler exceptionHandler;
    private long cardId;

    @FXML
    private TextField changeTitle;
    @FXML
    private TextArea changeDesc;

    @Inject
    public EditCardCtrl(ServerUtils server, ClientUtils client, MainCtrl mainCtrl, ExceptionHandler exceptionHandler) {
        this.server = server;
        this.client = client;
        this.mainCtrl = mainCtrl;
        this.exceptionHandler = exceptionHandler;
    }

    public void saveCardChanges() {
        Card card = client.getCard(cardId);
        card.setTitle(changeTitle.getText());
        card.setDescription(changeDesc.getText());
        server.updateCard(card);
        client.getCardCtrl(cardId).refresh();
        resetState();
        mainCtrl.showMainView();
    }

    public void loadData(long cardId) {
        this.cardId = cardId;
        Card card = client.getCard(cardId);
        changeTitle.setText(card.getTitle());
        changeDesc.setText(card.getDescription());
    }

    public void cancel() {
        resetState();
        mainCtrl.showMainView();
    }

    @FXML
    public void onKeyPressed(KeyEvent e) {
        if (KeyCode.ESCAPE == e.getCode()) {
            cancel();
        }
    }

    public void resetState() {
        this.changeTitle.setText("");
        this.changeDesc.setText("");
    }

    @Override
    public void revalidate() {
        if (client.getCardCtrls().containsKey(cardId)) {
            return;
        }
        mainCtrl.showMainView();
        exceptionHandler.clientException("Sorry, but the card you were editing or the list it was part of has been permanently deleted.");
    }
}
