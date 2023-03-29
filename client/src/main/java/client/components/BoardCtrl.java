package client.components;

import java.util.stream.IntStream;

import javax.inject.Inject;

import client.scenes.MainCtrl;
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

public class BoardCtrl implements Component<Board>, DBEntityCtrl<Board, CardList> {
    private final ClientUtils client;
    private final ComponentFactory factory;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @Getter
    private Board board;
    @FXML
    private HBox boardView;

    @Inject
    public BoardCtrl(ClientUtils client, ComponentFactory factory, ServerUtils server, MainCtrl mainCtrl) {
        this.client = client;
        this.factory = factory;
        this.server = server;
        this.mainCtrl = mainCtrl;
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

        //Todo - Find out why null cardLists are being added to lists upon certain operations. This is just a temporary fix, which is not elegant.
        for(int i = 0;i < board.getCardLists().size();i++) {
            if(board.getCardLists().get(i) == null) {
                board.getCardLists().remove(i);
                i--;
            }
        }

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
        client.getBoardCtrl().getBoard().setEditable(true);
    }

    public void remove() {
        removeChildren();
        client.setBoardCtrl(null);
    }

    public void removeChildren() {
        for (CardList cardList : board.getCardLists()) {
            client.getCardListCtrl(cardList.getId()).remove();
        }
    }

    public void replaceChild(CardList cardList) {
        int idx = IntStream.range(0, board.getCardLists().size())
            .filter(i -> board.getCardLists().get(i).getId() == cardList.getId())
            .findFirst()
            .orElse(-1);
        if (idx == -1)
            throw new IllegalStateException("Attempting to replace list in board that does not exist.");
        board.getCardLists().set(idx, cardList);
        cardList.setBoard(board);
    }

    private void switchSelectedCardList(int diff) {
        int cardListIdx = 0;
        if (client.getCardCtrl(client.getSelectedCardId()) != null) {
            cardListIdx = board.getCardLists().indexOf(client.getCardList(client.getCard(client.getSelectedCardId()).getCardList().getId())) + diff;
        }
        if (cardListIdx < 0 || cardListIdx >= board.getCardLists().size())
            return;
        CardList cardList = board.getCardLists().get(cardListIdx);
        if (cardList.getCards().size() == 0) { // If empty list, try next
            switchSelectedCardList(diff + Integer.signum(diff));
            return;
        }
        client.changeSelection(cardList.getCards().get(0).getId());
    }

    private void switchSelectedCard(int diff) {
        int cardIdx = 0;
        CardList cardList;
        if (client.getCardCtrl(client.getSelectedCardId()) != null) {
            cardList = client.getCard(client.getSelectedCardId()).getCardList();
            cardIdx = cardList.getCards().indexOf(client.getCard(client.getSelectedCardId())) + diff;
        } else {
            cardList = board.getCardLists().get(0);
        }
        if (cardIdx < 0
            || cardList.getCards() == null
            || cardList.getCards().size() == 0
            || cardIdx >= cardList.getCards().size()) {
            return;
        }
        client.changeSelection(cardList.getCards().get(cardIdx).getId());
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
            case ENTER:
                mainCtrl.showEditCard(client.getSelectedCardId());
                break;
            default:
                break;
        }
    }
}
