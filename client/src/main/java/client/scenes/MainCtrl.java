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

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;

public class MainCtrl {

    private Stage primaryStage;

    private ServerConnectCtrl serverConnectCtrl;
    private Scene connect;

    private BoardSettingsCtrl boardSettingsCtrl;
    private Scene settings;

    private MainViewCtrl mainViewCtrl;
    private Scene main;

    private CreateBoard createBoardCtrl;
    private Scene create;

    private AddCardCtrl addCardCtrl;
    private Scene add;

    public void initialize(Stage primaryStage,
                           Pair<ServerConnectCtrl, Parent> connect,
                           Pair<BoardSettingsCtrl, Parent> settings,
                           Pair<AddCardCtrl, Parent> add,
                           Pair<MainViewCtrl, Parent> main,
                           Pair<CreateBoard, Parent> create
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

        showConnect();
        primaryStage.show();
    }

    public void showConnect() {
        primaryStage.setTitle("Connect: Talio server");
        primaryStage.setResizable(false);
        primaryStage.setScene(connect);
    }

    public void showSettings(){
        primaryStage.setTitle("Board Settings");
        primaryStage.setScene(settings);
    }

    public void showAddCard(){
        primaryStage.setTitle("Add card");
        primaryStage.setScene(add);
    }

    public void showMainView() {
        primaryStage.setTitle("Main view");
        primaryStage.setScene(main);
    }

    public void showCreate() {
        primaryStage.setTitle("Create board");
        primaryStage.setScene(create);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}