package client.components;

import javax.inject.Inject;

import client.utils.ClientUtils;
import client.utils.ComponentFactory;
import client.utils.ServerUtils;
import commons.Board;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import lombok.Getter;

public class BoardCtrl implements Component<Board>, DBEntityCtrl<Board> {
    private final ClientUtils client;
    private final ComponentFactory factory;
    private final ServerUtils server;

    @Getter
    private Board board;
    @FXML
    private HBox boardView;

    private long selectedCardId = -1;

    @Inject
    public BoardCtrl(ClientUtils client, ComponentFactory factory, ServerUtils server) {
        this.client = client;
        this.factory = factory;
        this.server = server;
    }

    public Parent getNode() {
        return boardView;
    }

    @Override
    public void loadData(Board board) {
        if (this.board != null)
            removeChildren();
        this.board = board;
        client.clearBoardData();
        client.setBoardCtrl(this);

        final long listWidthPlusGap = 300;
        boardView.setMinWidth(board.getCardLists().size() * listWidthPlusGap);

        for (CardList cardList : board.getCardLists()) {
            if (cardList == null)
                continue;
            CardListCtrl cardListCtrl = factory.create(CardListCtrl.class, cardList);
            boardView.getChildren().add(cardListCtrl.getNode());
        }
    }

    public void refresh() {
        boardView.getChildren().clear();
        loadData(server.getBoard(board.getId()));
    }

    public void remove() {
        removeChildren();
        client.setBoardCtrl(null);
    }

    public void removeChildren() {
        for (CardList cardList : board.getCardLists()) {
            client.getCardListCtrls().get(cardList.getId()).remove();
        }
    }

    private void changeSelection(long selectedCardId) {
        CardCtrl cardCtrl = client.getCardCtrl(this.selectedCardId);
        if (cardCtrl != null)
            cardCtrl.unhighlight();
        this.selectedCardId = selectedCardId;
        client.getCardCtrl(selectedCardId).highlight();
    }

    private void switchSelectedCardList(int diff) {
        int cardListIdx = 0;
        if (client.getCardCtrl(selectedCardId) != null) {
            cardListIdx = board.getCardLists().indexOf(client.getCardList(client.getCard(selectedCardId).getCardList().getId())) + diff;
        }
        if (cardListIdx < 0 || cardListIdx >= board.getCardLists().size())
            return;
        CardList cardList = board.getCardLists().get(cardListIdx);
        if (cardList.getCards().size() == 0) { // If empty list, try next
            switchSelectedCardList(diff + Integer.signum(diff));
            return;
        }
        changeSelection(cardList.getCards().get(0).getId());
    }

    private void switchSelectedCard(int diff) {
        int cardIdx = 0;
        CardList cardList;
        if (client.getCardCtrl(selectedCardId) != null) {
            cardList = client.getCard(selectedCardId).getCardList();
            cardIdx = cardList.getCards().indexOf(client.getCard(selectedCardId)) + diff;
        } else {
            cardList = board.getCardLists().get(0);
        }
        if (cardIdx < 0
            || cardList.getCards() == null
            || cardList.getCards().size() == 0
            || cardIdx >= cardList.getCards().size()) {
            return;
        }
        changeSelection(cardList.getCards().get(cardIdx).getId());
    }

    public void handleKeyEvent(KeyEvent e) {
        if (board.getCardLists() == null || board.getCardLists().size() == 0)
            return;
        switch (e.getCode()) {
            case LEFT:
                switchSelectedCardList(-1);
                break;
            case RIGHT:
                switchSelectedCardList(1);
                break;
            case UP:
                switchSelectedCard(-1);
                break;
            case DOWN:
                switchSelectedCard(1);
                break;
            default:
                break;
        }
    }
}
