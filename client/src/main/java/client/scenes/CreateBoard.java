package client.scenes;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CreateBoard {

    @FXML
    private TextField boardName;

    @FXML
    void buttonCancel(ActionEvent event) {
        System.out.println("You clicked cancel");
    }

    @FXML
    void buttonCreate(ActionEvent event) {
        System.out.println("You entered: " + boardName.getText());
        boardName.setText("");
    }

}
