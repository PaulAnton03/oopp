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

import javax.inject.Inject;

import client.utils.ClientPreferences;
import client.utils.ClientUtils;
import client.utils.ComponentFactory;
import client.utils.KeyEventHandler;
import client.utils.Logger;
import client.utils.ServerUtils;
import commons.Board;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Pair;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class MainCtrl {
    private final ServerUtils server;
    private final ClientUtils client;
    private final ComponentFactory factory;
    private final ClientPreferences clientPreferences;

    @Getter
    private Stage primaryStage;

    private ServerConnectCtrl serverConnectCtrl;
    private Scene connect;

    private BoardSettingsCtrl boardSettingsCtrl;
    private Scene settings;

    @Getter
    private MainViewCtrl mainViewCtrl;
    private Scene main;

    private CreateBoardCtrl createBoardCtrl;
    private Scene create;

    private AddCardCtrl addCardCtrl;
    private Scene add;

    @Getter
    private JoinBoardsCtrl joinBoardsCtrl;
    private Scene join;

    @Getter
    private EditCardCtrl editCardCtrl;
    private Scene editCard;

    private AddListCtrl addListCtrl;
    private Scene addList;

    private ListSettingsCtrl editListCtrl;
    private Scene editList;

    private PasswordProtectedCtrl passwordProtectedCtrl;
    private Scene passwordProtected;

    @Getter
    private AdminPasswordCtrl adminPasswordCtrl;
    private Scene adminPassword;

    private ThemeEditorCtrl themeEditorCtrl;
    private Scene themeEdit;

    @Getter
    private SceneCtrl activeCtrl;

    @Inject
    public MainCtrl(ServerUtils server, ClientUtils client, ComponentFactory factory, ClientPreferences clientPreferences) {
        this.server = server;
        this.client = client;
        this.factory = factory;
        this.clientPreferences = clientPreferences;
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
        private Pair<AdminPasswordCtrl, Parent> adminPsw;
        private Pair<ThemeEditorCtrl, Parent> themeEditor;
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

        this.adminPasswordCtrl = builder.getAdminPsw().getKey();
        this.adminPassword = new Scene(builder.getAdminPsw().getValue());

        this.themeEditorCtrl = builder.getThemeEditor().getKey();
        this.themeEdit = new Scene(builder.getThemeEditor().getValue());

        primaryStage.setResizable(true);
        showConnect();
        primaryStage.show();
        primaryStage.setWidth(1000);
        primaryStage.setHeight(600);
        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, new KeyEventHandler(client, this));
    }

    private void showScene(Scene scene) {
        double w = primaryStage.getWidth();
        double h = primaryStage.getHeight();
        Region region = (Region)scene.getRoot();
        primaryStage.setScene(scene);
        primaryStage.setWidth(Math.max(Double.isNaN(w) ? 0 : w, region.getMinWidth()));
        primaryStage.setHeight(Math.max(Double.isNaN(h) ? 0 : h, region.getMinHeight()));
    }

    public void showEditCard(long cardId) {
        editCardCtrl.loadData(cardId);
        primaryStage.setTitle("Edit Card");
        showScene(editCard);
        activeCtrl = editCardCtrl;
    }

    public void showAdminPasswordProtected() {
        primaryStage.setTitle("Admin password");
        showScene(adminPassword);
        activeCtrl = adminPasswordCtrl;
    }

    public void showConnect() {
        primaryStage.setTitle("Connect: Talio server");
        showScene(connect);
        activeCtrl = serverConnectCtrl;
    }

    public void showSettings() {
        boardSettingsCtrl.loadData();
        primaryStage.setTitle("Board Settings");
        showScene(settings);
        activeCtrl = boardSettingsCtrl;
    }

    public void showAddCard(long cardListId) {
        addCardCtrl.loadData(cardListId);
        primaryStage.setTitle("Add card");
        showScene(add);
        activeCtrl = addCardCtrl;
    }

    public void showMainView(Board board) {
        mainViewCtrl.loadData(board);
        primaryStage.setTitle("Main view");
        showScene(main);
        clientPreferences.setDefaultBoardId(board.getId());
        boolean okPassword = board.getPassword() != null && !board.getPassword().isEmpty();
        if (board.isEditable() && okPassword)
            clientPreferences.setPasswordForBoard(board.getId(), board.getPassword());
        clientPreferences.addJoinedBoard(board.getId());
        Logger.log("Showing main view for board " + board);
        activeCtrl = mainViewCtrl;
    }

    public void showMainView() {
        activeCtrl = mainViewCtrl;
        Long boardId = clientPreferences.getDefaultBoardId().orElse(Long.valueOf(-1));
        if (boardId == -1) {
            Logger.log("No default board, cannot show main view");
            this.showJoin();
            return;
        }
        primaryStage.setTitle("Main view");
        showScene(main);
        Board board;
        try {
            board = server.getBoard(boardId);
        } catch (Exception e) {
            Logger.log("Default board not found, cannot show main view");
            this.showJoin();
            return;
        }

        String password = clientPreferences.getPasswordForBoard(boardId).orElse(null);
        if (board.getPassword() != null && !board.getPassword().equals(password)) {
            Logger.log("Saved password for board " + boardId + " is not correct, cannot show main view.", Logger.LogLevel.WARN);
            showJoin();
            return;
        }
        Logger.log("Connecting to the last used board.");
        showMainView(board);
        client.getBoardCtrl().getBoard().setEditable(true);
    }

    public void showCreate() {
        createBoardCtrl.loadData();
        primaryStage.setTitle("Create board");
        showScene(create);
        activeCtrl = createBoardCtrl;
    }

    public void showJoin() {
        primaryStage.setTitle("Join boards");
        showScene(join);
        joinBoardsCtrl.populateBoards();
        activeCtrl = joinBoardsCtrl;
    }

    public void showAddList() {
        primaryStage.setTitle("Add list");
        showScene(addList);
        activeCtrl = addListCtrl;
    }

    public void showListSettings(long cardListId) {
        primaryStage.setTitle("Edit list");
        editListCtrl.loadData(cardListId);
        showScene(editList);
        activeCtrl = editListCtrl;
    }

    public void showPasswordProtected(Board pswProtectedBoard) {
        primaryStage.setTitle("Password Protected Board");
        passwordProtectedCtrl.loadData(pswProtectedBoard);
        showScene(passwordProtected);
        activeCtrl = passwordProtectedCtrl;
    }

    public void showThemeEditor() {
        activeCtrl = themeEditorCtrl;
        primaryStage.setTitle("Theme editor");
        themeEditorCtrl.loadData();
        showScene(themeEdit);
    }

    public void stop() {
        server.stop();
    }
}
