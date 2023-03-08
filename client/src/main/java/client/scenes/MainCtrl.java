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

    private QuoteOverviewCtrl overviewCtrl;
    private Scene overview;

    private AddQuoteCtrl addCtrl;
    private Scene add;

    private MainViewCtrl mainViewCtrl;

    private Scene main;

    private CreateBoard createBoardCtrl;
    private Scene create;

    public void initialize(Stage primaryStage, Pair<ServerConnectCtrl, Parent> connect,
                           Pair<QuoteOverviewCtrl, Parent> overview,
                           Pair<AddQuoteCtrl, Parent> add,
                           Pair<MainViewCtrl, Parent> main,
                           Pair<CreateBoard, Parent> create) {
        this.primaryStage = primaryStage;
        this.serverConnectCtrl = connect.getKey();
        this.connect = new Scene(connect.getValue());
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.addCtrl = add.getKey();
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

    public void showOverview() {
        primaryStage.setTitle("Quotes: Overview");
        primaryStage.setScene(overview);
        overviewCtrl.refresh();
    }

    public void showAdd() {
        primaryStage.setTitle("Quotes: Adding Quote");
        primaryStage.setScene(add);
        add.setOnKeyPressed(e -> addCtrl.keyPressed(e));
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public void showMain() {
        primaryStage.setTitle("Main page");
        primaryStage.setScene(main);
    }

    public void showCreate() {
        primaryStage.setTitle("Create board");
        primaryStage.setScene(create);
    }
}