package client.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class MainViewCtrl {
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

    public MainViewCtrl(MainCtrl mainCtrl, Button navbarAddButton) {
        this.mainCtrl = mainCtrl;
        this.navbarAddButton = navbarAddButton;
    }

    @FXML
    void btnAddClicked(ActionEvent event) {
        mainCtrl.showAddCard();
    }

    @FXML
    void btnBackClicked(ActionEvent event) {
        /*showBack();*/
    }

    @FXML
    void btnCreateClicked(ActionEvent event) {
        /*showCreate();*/
    }

    @FXML
    void btnJoinClicked(ActionEvent event) {
        /*showJoin();*/
    }

    @FXML
    void btnSettingsClicked(ActionEvent event) {
        /*showSettings();*/
    }

    @FXML
    void testHover(MouseEvent event) {
        navbarJoinButton.setStyle("-fx-background-color: #000000");
    }

}
