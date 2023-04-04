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
package client;

import client.scenes.*;
import client.scenes.MainCtrl.ScenesBuilder;
import client.utils.ExceptionHandler;
import com.google.inject.Injector;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.google.inject.Guice.createInjector;

public class Main extends Application {

    public static final Injector INJECTOR = createInjector(new MyModule());
    public static final MyFXML FXML = new MyFXML(INJECTOR);
    private MainCtrl mainCtrl = null;

    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
//        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());
//
        ScenesBuilder builder = new ScenesBuilder();

        builder.setConnect(FXML.load(ServerConnectCtrl.class, "client", "scenes", "ServerConnect.fxml"));
        builder.setMain(FXML.load(MainViewCtrl.class, "client", "scenes", "MainView.fxml"));
        builder.setCreate(FXML.load(CreateBoardCtrl.class, "client", "scenes", "CreateBoard.fxml"));
        builder.setSettings(FXML.load(BoardSettingsCtrl.class, "client", "scenes", "BoardSettings.fxml"));
        builder.setAddCard(FXML.load(AddCardCtrl.class, "client", "scenes", "AddCard.fxml"));
        builder.setJoin(FXML.load(JoinBoardsCtrl.class, "client", "scenes", "JoinBoards.fxml"));
        builder.setEditCard(FXML.load(EditCardCtrl.class, "client", "scenes", "EditCard.fxml"));
        builder.setAddList(FXML.load(AddListCtrl.class, "client", "scenes", "AddList.fxml"));
        builder.setEditList(FXML.load(ListSettingsCtrl.class, "client", "scenes", "ListSettings.fxml"));
        builder.setPswProtected(FXML.load(PasswordProtectedCtrl.class, "client", "scenes", "PasswordProtected.fxml"));
        builder.setAdminPsw(FXML.load(AdminPasswordCtrl.class, "client", "scenes", "AdminPassword.fxml"));
        builder.setTagSettings(FXML.load(TagSettingsCtrl.class, "client", "scenes", "TagSettings.fxml"));

        mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, builder);

        primaryStage.setOnCloseRequest(e -> {
            System.exit(0);
        });

    }

    @Override
    public void stop() { mainCtrl.stop(); }
}
