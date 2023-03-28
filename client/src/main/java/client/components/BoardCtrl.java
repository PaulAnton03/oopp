package client.components;

import client.utils.ClientUtils;
import client.utils.ComponentFactory;
import client.utils.ServerUtils;
import commons.Board;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import lombok.Getter;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BoardCtrl implements Component<Board> {
    private final ClientUtils client;
    private final ComponentFactory factory;
    private final ServerUtils server;

    @Getter
    private Board board;
    @Getter
    private final Map<Long, CardListCtrl> cardListCtrls = new HashMap<>();
    @FXML
    private HBox boardView;

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
        List<CardList> sortedLists = board.getCardLists().stream()
                .sorted(Comparator.comparingDouble(CardList::getId))
                .collect(Collectors.toList());

        for (CardList cardList : sortedLists) {
            CardListCtrl cardListCtrl = factory.create(CardListCtrl.class, cardList);
            cardListCtrls.put(cardList.getId(), cardListCtrl);
            boardView.getChildren().add(cardListCtrl.getNode());
        }
    }

    public void refresh() {
        boardView.getChildren().clear();
        try {
            loadData(server.getBoard(board.getId()));
        } catch (Exception e) {
            System.out.println("Refreshed unsuccessfully");
        }
    }
}
