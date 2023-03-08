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

    private QuoteOverviewCtrl overviewCtrl;
    private Scene overview;

    private AddQuoteCtrl addCtrl;
    private Scene add;

    private AddCardCtrl addCardCtrl;
    private Scene addCard;

    private MainViewCtrl mainViewCtrl;
    private Scene mainView;

    public void initialize(Stage primaryStage, Pair<ServerConnectCtrl, Parent> connect,
                           Pair<BoardSettingsCtrl, Parent> settings,
                           Pair<QuoteOverviewCtrl, Parent> overview,
                           Pair<AddQuoteCtrl, Parent> add,
                           Pair<AddCardCtrl, Parent> addCard,
                           Pair<MainViewCtrl, Parent> mainView) {
        this.primaryStage = primaryStage;
        this.serverConnectCtrl = connect.getKey();
        this.connect = new Scene(connect.getValue());
        this.boardSettingsCtrl = settings.getKey();
        this.settings = new Scene(settings.getValue());
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());

        this.addCtrl = add.getKey();
        this.add = new Scene(add.getValue());

        this.addCardCtrl = addCard.getKey();
        this.addCard = new Scene(addCard.getValue());

        this.mainViewCtrl = mainView.getKey();
        this.mainView = new Scene(mainView.getValue());

        showConnect();
        primaryStage.show();
    }

    public void showConnect() {
        primaryStage.setTitle("Connect: Talio server");
        primaryStage.setResizable(false);
        primaryStage.setScene(connect);
    }

    public void showSettigns(){
        primaryStage.setTitle("Board Settings");
        primaryStage.setScene(settings);
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

    public void showAddCard(){
        primaryStage.setTitle("Add card");
        primaryStage.setScene(addCard);
    }

    public void showMainView() {
        primaryStage.setTitle("Main view");
        primaryStage.setScene(mainView);
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }
}