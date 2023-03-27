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

import client.components.CardCtrl;
import client.utils.ClientUtils;
import client.utils.ComponentFactory;
import client.utils.Logger;
import client.utils.ServerUtils;
import commons.Board;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.inject.Inject;

public class MainCtrl {
    private final ServerUtils server;
    private final ClientUtils client;
    private final ComponentFactory factory;

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

    private PasswordProtectedCtrl passwordProtectedCtrl;
    private Scene passwordProtected;

    @Inject
    public MainCtrl(ServerUtils server, ClientUtils client, ComponentFactory factory) {
        this.server = server;
        this.client = client;
        this.factory = factory;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class ScenesBuilder {
        private Pair<ServerConnectCtrl, Parent> connect;
        private Pair<BoardSettingsCtrl, Parent> settings;
        private Pair<AddCardCtrl, Parent> addCard;
        private Pair<MainViewCtrl, Parent> main;
        private Pair<CreateBoardCtrl, Parent> create;
        private Pair<JoinBoardsCtrl, Parent> join;
        private Pair<AddListCtrl, Parent> addList;
        private Pair<ListSettingsCtrl, Parent> editList;
        private Pair<EditCardCtrl, Parent> editCard;
        private Pair<PasswordProtectedCtrl, Parent> pswProtected;
    }

    public void initialize(Stage primaryStage, ScenesBuilder builder) {
        this.primaryStage = primaryStage;

        this.serverConnectCtrl = builder.getConnect().getKey();
        this.connect = new Scene(builder.getConnect().getValue());

        this.boardSettingsCtrl = builder.getSettings().getKey();
        this.settings = new Scene(builder.getSettings().getValue());

        this.addCardCtrl = builder.getAddCard().getKey();
        this.add = new Scene(builder.getAddCard().getValue());

        this.mainViewCtrl = builder.getMain().getKey();
        this.main = new Scene(builder.getMain().getValue());

        this.createBoardCtrl = builder.getCreate().getKey();
        this.create = new Scene(builder.getCreate().getValue());

        this.joinBoardsCtrl = builder.getJoin().getKey();
        this.join = new Scene(builder.getJoin().getValue());

        this.editCardCtrl = builder.getEditCard().getKey();
        this.editCard = new Scene(builder.getEditCard().getValue());

        this.addListCtrl = builder.getAddList().getKey();
        this.addList = new Scene(builder.getAddList().getValue());

        this.editListCtrl = builder.getEditList().getKey();
        this.editList = new Scene(builder.getEditList().getValue());

        this.passwordProtectedCtrl = builder.getPswProtected().getKey();
        this.passwordProtected = new Scene(builder.getPswProtected().getValue());

        primaryStage.setResizable(true);
        showConnect();
        primaryStage.show();
    }

    public void showEditCard(CardCtrl cardCtrl){
        client.setActiveCardCtrl(cardCtrl);
        editCardCtrl.loadData(cardCtrl.getCard());
        primaryStage.setTitle("Edit Card");
        primaryStage.setScene(editCard);
    }

    public void showConnect() {
        primaryStage.setTitle("Connect: Talio server");
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
        mainViewCtrl.loadData(board);
        primaryStage.setTitle("Main view");
        primaryStage.setScene(main);
        Logger.log("Showing main view for board " + board);
    }

    public void showMainView() {
        if (client.getActiveBoard() == null) {
            Logger.log("No active board, cannot show main view");
            this.showJoin();
            return;
        }
        client.getActiveBoardCtrl().refresh();
        primaryStage.setTitle("Main view");
        primaryStage.setScene(main);
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

    public void showPasswordProtected(Board pswProtectedBoard) {
        primaryStage.setTitle("Password Protected Board");
        passwordProtectedCtrl.loadData(pswProtectedBoard);
        primaryStage.setScene(passwordProtected);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void stop() {
        server.stop();
    }
}
