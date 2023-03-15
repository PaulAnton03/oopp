package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Board;
import commons.Card;
import commons.CardList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

public class MainViewCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML private HBox cardLists;

    @Inject
    public MainViewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @FXML
    void btnAddClicked(ActionEvent event) {
        mainCtrl.showAddCard();
    }

    @FXML
    void btnBackClicked(ActionEvent event) {
        mainCtrl.showConnect();
    }

    @FXML
    void btnCreateClicked(ActionEvent event) {
        mainCtrl.showCreate();
    }

    @FXML
    void btnJoinClicked(ActionEvent event) {
        mainCtrl.showJoin();
    }

    @FXML
    void btnSettingsClicked(ActionEvent event) {
        mainCtrl.showSettings();
    }

    /**
     * Initializes the main view with the board provided
     * @param board - Board for which card lists are displayed. If null, empty board is displayed.
     */
    void onSetup(Board board) {
        List<Node> children = cardLists.getChildren();
        List<CardList> cards;
        if(board != null)
            cards = board.getCards();
        else
            cards = new ArrayList<>();

        for (CardList list : cards) {
            Text text = new Text(list.getTitle());
            text.setFont(new Font(22));
            Button cancel = new Button("x");
            // Todo - Make button to actually delete a list
            cancel.setOnAction(event -> System.out.println("You want to delete a list"));
            AnchorPane top = new AnchorPane(text, cancel);
            top.setPrefHeight(50.0);
            top.setPrefWidth(200.0);

            AnchorPane.setTopAnchor(text, 10.0);
            AnchorPane.setLeftAnchor(text, 5.0);
            AnchorPane.setTopAnchor(cancel, 15.0);
            AnchorPane.setRightAnchor(cancel, 10.0);

            List<String> taskList = new ArrayList<>();
            for(Card card : list.getCardList()) {
                taskList.add(card.getTitle());
            }
            ObservableList<String> tasks = FXCollections.observableList(taskList);
            ListView<String> cardList = new ListView<>(tasks);

            AnchorPane displayList = new AnchorPane(top, cardList);
            AnchorPane.setTopAnchor(top, 0.0);
            AnchorPane.setRightAnchor(top, 0.0);
            AnchorPane.setLeftAnchor(top, 0.0);
            AnchorPane.setTopAnchor(cardList, 50.0);
            AnchorPane.setBottomAnchor(cardList, 10.0);

            children.add(displayList);
        }
    }
}
