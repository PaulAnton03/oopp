package client.scenes;

import client.components.BoardJoinCtrl;
import client.utils.ComponentFactory;
import client.components.PasswordProtectedCtrl;
import client.utils.ServerUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.inject.Inject;
import java.util.stream.Collectors;

public class JoinBoardsCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final ComponentFactory factory;

    @FXML
    private TextField boardPasswordField;
    @FXML
    private HBox grid;

    @FXML
    private VBox boardPopulation;

    public void populateBoards() {
        boardPopulation.getChildren().clear();

        var boardJoinNodes = server.getBoards().stream()
                .map(board -> factory.create(BoardJoinCtrl.class, board).getNode())
                .collect(Collectors.toList());
        boardPopulation.getChildren().addAll(boardJoinNodes);
    }

    @Inject
    public JoinBoardsCtrl(ServerUtils server, MainCtrl mainCtrl, ComponentFactory factory) {
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.factory = factory;
    }

    @FXML
    void btnBackClicked(ActionEvent event) {
        boardPasswordField.setText("");
        mainCtrl.showMainView();
    }

    @FXML
    void btnClearClicked(ActionEvent event) {
        boardPasswordField.setText("");
    }

    @FXML
    void btnCreateClicked(ActionEvent event) {
        mainCtrl.showCreate();
    }

    private Board requestedPassword;

    public void requestPassword(Board requestedPassword) {
        this.requestedPassword = requestedPassword;

        var pair = myFXML.load(PasswordProtectedCtrl.class, "client", "components", "PasswordProtected.fxml");
        PasswordProtectedCtrl ctrl = pair.getKey();
        var component = pair.getValue();

        mainCtrl.getPrimaryStage().setTitle("Password Protected Board");
        mainCtrl.getPrimaryStage().setScene(new Scene(component));

        ctrl.setPasswordBoard(requestedPassword);
    }
}
