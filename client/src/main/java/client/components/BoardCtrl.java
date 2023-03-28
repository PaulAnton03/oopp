package client.components;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import client.utils.ClientUtils;
import client.utils.ComponentFactory;
import client.utils.Logger;
import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
import commons.CardList;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import lombok.Getter;

public class BoardCtrl implements Component<Board> {
    private final ClientUtils client;
    private final ComponentFactory factory;
    private final ServerUtils server;

    @Getter
    private Board board;
    @Getter
    private Map<Long, CardListCtrl> cardListCtrls = new HashMap<>();
    @FXML
    private HBox boardView;

    private long selectedCardListId = -1;
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
        this.board = board;
        client.setActiveBoardCtrl(this);

        final long listWidthPlusGap = 200;
        boardView.setMinWidth(board.getCardLists().size() * listWidthPlusGap);
        List<CardList> sortedLists = board.getCardLists().stream().filter(c -> c != null)
                .sorted(Comparator.comparingDouble(CardList::getId))
                .collect(Collectors.toList());

        for (CardList cardList : sortedLists) {
            CardListCtrl cardListCtrl = factory.create(CardListCtrl.class, cardList);
            cardListCtrls.put(cardList.getId(), cardListCtrl);
            boardView.getChildren().add(cardListCtrl.getNode());
        }

        // Find new controller assigned to selected card and highlight
        if (selectedCardId != -1) {
            boolean foundCardCtrl = false;
            for (CardListCtrl cardListCtrl : cardListCtrls.values()) {
                CardCtrl cardCtrl = cardListCtrl.getCardCtrls().get(selectedCardId);
                if (cardCtrl != null) {
                    selectedCardId = cardCtrl.getCard().getId();
                    selectedCardListId = cardListCtrl.getCardList().getId();
                    cardCtrl.highlight();
                    foundCardCtrl = true;
                    break;
                }
            }
            if (!foundCardCtrl) {
                selectedCardId = -1;
                selectedCardListId = -1;
            }
        }
    }

    public void refresh() {
        boardView.getChildren().clear();
        this.cardListCtrls = new HashMap<>();
        try {
            loadData(server.getBoard(board.getId()));
        } catch (Exception e) {
            Logger.log("Failed to refresh board " + board + ". Error: " + e.getMessage());
        }
    }

    private CardCtrl getSelectedCardCtrl() {
        CardListCtrl cardListCtrl = getCardListCtrls().get(selectedCardListId);
        return (cardListCtrl == null) ? null : cardListCtrl.getCardCtrls().get(selectedCardId);
    }

    private void switchSelectedCardList(int diff) {
        int cardListIdx = 0;
        if (getSelectedCardCtrl() != null) {
            cardListIdx = board.getCardLists().indexOf(getCardListCtrls().get(selectedCardListId).getCardList()) + diff;
        }
        if (cardListIdx < 0 || cardListIdx >= board.getCardLists().size()) {
            return;
        }
        CardList cardList = board.getCardLists().get(cardListIdx);
        if (cardList.getCards().size() == 0) { // If empty list, try next
            switchSelectedCardList(diff + Integer.signum(diff));
            return;
        }
        if (getSelectedCardCtrl() != null) {
            getSelectedCardCtrl().unhighlight();
        }
        selectedCardListId = cardList.getId();
        selectedCardId = cardList.getCards().get(0).getId();
        getSelectedCardCtrl().highlight();
    }

    private void switchSelectedCard(int diff) {
        int cardIdx = 0;
        CardListCtrl cardListCtrl;
        if (getSelectedCardCtrl() != null) {
            cardListCtrl = getCardListCtrls().get(board.getCardLists().get(0).getId());
        } else {
            cardListCtrl = getCardListCtrls().get(selectedCardListId);
            cardIdx = cardListCtrl.getCardList().getCards().indexOf(cardListCtrl.getCardCtrls().get(selectedCardId).getCard()) + diff;
        }
        if (cardListCtrl.getCardList().getCards().size() == 0 || cardIdx < 0 || cardIdx >= cardListCtrl.getCardList().getCards().size()) {
            return;
        }
        if (getSelectedCardCtrl() != null) {
            getSelectedCardCtrl().unhighlight();
        }
        selectedCardId = cardListCtrl.getCardList().getCards().get(cardIdx).getId();
        selectedCardListId = cardListCtrl.getCardList().getId();
        getSelectedCardCtrl().highlight();
    }

    public void handleKeyEvent(KeyEvent e) {
        if (getCardListCtrls().size() == 0)
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

    /**
     * This method puts a new CardListCtrl for the added cardList
     * in the cardListCtrls map and adds it to the interface
     *
     * @param cardList the new cardList that needs to be displayed for the user
     */
    public void displayList(CardList cardList) {
        if (cardListCtrls.containsKey(cardList.getId())) {
            System.out.println("This cardList is already here");
            return;
        }
        CardListCtrl cardListCtrl = factory.create(CardListCtrl.class, cardList);
        cardListCtrls.put(cardList.getId(), cardListCtrl);
        Platform.runLater(() -> {
            boardView.getChildren().add(cardListCtrl.getNode());
        });
    }

    /**
     * This method removes the deleted cardList's CardListCtrl from the cardListCtrls map
     * and removes it from the interface
     *
     * @param cardList the cardList that needs to be deleted from the user's display
     */
    public void removeCardList(CardList cardList) {
        CardListCtrl cardListCtrl = cardListCtrls.get(cardList.getId());
        if (cardListCtrl != null) {
            cardListCtrls.remove(cardList.getId());
            Platform.runLater(() -> {
                boardView.getChildren().remove(cardListCtrl.getNode());
            });
        }
    }
}
