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

import static com.google.inject.Guice.createInjector;

import java.io.IOException;
import java.net.URISyntaxException;

import client.scenes.*;
import client.utils.ExceptionHandler;

import com.google.inject.Injector;

import javafx.application.Application;
import javafx.stage.Stage;
import lombok.Getter;

public class Main extends Application {

    private static final Injector INJECTOR = createInjector(new MyModule());
    @Getter
    private static final MyFXML FXML = new MyFXML(INJECTOR);
    private MainCtrl mainCtrl = null;

    public static void main(String[] args) throws URISyntaxException, IOException {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler());

        var connect = FXML.load(ServerConnectCtrl.class, "client", "scenes", "ServerConnect.fxml");
        var main = FXML.load(MainViewCtrl.class, "client", "scenes", "MainView.fxml");
        var create = FXML.load(CreateBoardCtrl.class, "client", "scenes", "CreateBoard.fxml");
        var settings = FXML.load(BoardSettingsCtrl.class, "client", "scenes", "BoardSettings.fxml");
        var add = FXML.load(AddCardCtrl.class, "client", "scenes", "AddCard.fxml");
        var join = FXML.load(JoinBoardsCtrl.class, "client", "scenes", "JoinBoards.fxml");
        var addList = FXML.load(AddListCtrl.class, "client", "scenes", "AddList.fxml");
        var editList = FXML.load(ListSettingsCtrl.class, "client", "scenes", "ListSettings.fxml");

        var mainCtrl = INJECTOR.getInstance(MainCtrl.class);
        mainCtrl.initialize(primaryStage, connect, settings, add, main, create, join, addList, editList);
    }
}