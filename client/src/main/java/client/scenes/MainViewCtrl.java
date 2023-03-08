package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class MainViewCtrl {

    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private Button navbarAddButton;

    @FXML
    private Button navbarBackButton;

    @FXML
    private Button navbarBoardSettings;

    @FXML
    private Button navbarCreateButton;

    @FXML
    private Button navbarJoinButton;

    @Inject
    public MainViewCtrl(ServerUtils server, MainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    @FXML
    void btnAddClicked(ActionEvent event) {
        System.out.println("Add page not added yet!!");
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
        System.out.println("Join page not added yet!!");
    }

    @FXML
    void btnSettingsClicked(ActionEvent event) {
        System.out.println("Settings page not added yet!!");
    }

}
