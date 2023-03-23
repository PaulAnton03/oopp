/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import client.Main;
import client.MyFXML;
import client.components.CardCtrl;
import client.components.CardListCtrl;
import client.utils.ClientUtils;
import client.utils.Logger;
import commons.Board;
import commons.Card;
import commons.CardList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import lombok.Getter;

import javax.inject.Inject;

public class MainCtrl {
    private final ClientUtils client;
    private final MyFXML myFXML = Main.getFXML();
    @Getter
    private Stage primaryStage;

    private ServerConnectCtrl serverConnectCtrl;
    private Scene connect;

    private BoardSettingsCtrl boardSettingsCtrl;
    private Scene settings;

    private MainViewCtrl mainViewCtrl;
    private Scene main;

    private CreateBoardCtrl createBoardCtrl;
    private Scene create;

    private AddCardCtrl addCardCtrl;
    private Scene add;

    private JoinBoardsCtrl joinBoardsCtrl;
    private Scene join;

    private EditCardCtrl editCardCtrl;
    private Scene editCard;

    private AddListCtrl addListCtrl;
    private Scene addList;

    private ListSettingsCtrl editListCtrl;
    private Scene editList;

    @Inject
    public MainCtrl(ClientUtils client) {
        this.client = client;
    }

    public void initialize(Stage primaryStage,
                           Pair<ServerConnectCtrl, Parent> connect,
                           Pair<BoardSettingsCtrl, Parent> settings,
                           Pair<AddCardCtrl, Parent> add,
                           Pair<MainViewCtrl, Parent> main,
                           Pair<CreateBoardCtrl, Parent> create,
                           Pair<JoinBoardsCtrl, Parent> join,
                           Pair<AddListCtrl, Parent> list,
                           Pair<ListSettingsCtrl, Parent> edit,
                           Pair<EditCardCtrl, Parent> editCard
    ) {
        this.primaryStage = primaryStage;

        this.serverConnectCtrl = connect.getKey();
        this.connect = new Scene(connect.getValue());

        this.boardSettingsCtrl = settings.getKey();
        this.settings = new Scene(settings.getValue());

        this.addCardCtrl = add.getKey();
        this.add = new Scene(add.getValue());

        this.mainViewCtrl = main.getKey();
        this.main = new Scene(main.getValue());

        this.createBoardCtrl = create.getKey();
        this.create = new Scene(create.getValue());

        this.joinBoardsCtrl = join.getKey();
        this.join = new Scene(join.getValue());

        this.editCardCtrl = editCard.getKey();
        this.editCard = new Scene(editCard.getValue());

        this.addListCtrl = list.getKey();
        this.addList = new Scene(list.getValue());

        this.editListCtrl = edit.getKey();
        this.editList = new Scene(edit.getValue());

        showConnect();
        primaryStage.show();
    }

    public void showEditCard(CardCtrl cardCtrl){
        editCardCtrl.setCardCtrl(cardCtrl);
        primaryStage.setTitle("Edit Card");
        primaryStage.setScene(editCard);
    }

    public void showConnect() {
        primaryStage.setTitle("Connect: Talio server");
        primaryStage.setResizable(false);
        primaryStage.setScene(connect);
    }

    public void showSettings() {
        primaryStage.setTitle("Board Settings");
        primaryStage.setScene(settings);
    }

    public void showAddCard() {
        primaryStage.setTitle("Add card");
        primaryStage.setScene(add);
    }

    public void showMainView(Board board) {
        primaryStage.setTitle("Main view");
        mainViewCtrl.onSetup(board);
        primaryStage.setScene(main);
        Logger.log("Showing main view for board " + board);
    }

    public void showMainView() {
        if (client.getActiveBoard() == null) {
            Logger.log("No active board, cannot show main view");
            this.showJoin();
            return;
        }
        showMainView(client.getActiveBoard());
    }

    public void showCreate() {
        primaryStage.setTitle("Create board");
        primaryStage.setScene(create);
    }

    public void showJoin() {
        primaryStage.setTitle("Join boards");
        primaryStage.setScene(join);
        joinBoardsCtrl.populateBoards();
    }

    public void showAddList() {
        primaryStage.setTitle("Add list");
        primaryStage.setScene(addList);
    }

    public void showListSettings() {
        primaryStage.setTitle("Edit list");
        primaryStage.setScene(editList);
    }

    public Pair<CardCtrl, Parent> createNewCard() {
        return myFXML.load(CardCtrl.class, "client", "components", "Card.fxml");
    }

    public Pair<CardListCtrl, Parent> createNewCardList() {
        return myFXML.load(CardListCtrl.class, "client", "components", "CardList.fxml");
    }

    public Parent createCard(Card card) {
        var pair = createNewCard();
        CardCtrl cardCtrl = pair.getKey();
        var newCard = pair.getValue();
        cardCtrl.loadData(card);
        return newCard;
    }

    public Parent createCardList(CardList cardList) {
        var pair = createNewCardList();
        CardListCtrl cardListCtrl = pair.getKey();
        var newCardList = pair.getValue();
        cardListCtrl.loadData(cardList);
        return newCardList;
    }

    public void stop() {
        this.client.getServer().stop();
    }
}
