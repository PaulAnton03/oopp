package client.components;

import client.scenes.MainCtrl;
import client.utils.ClientUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.SubTask;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.net.URL;
import java.util.ResourceBundle;

@EqualsAndHashCode
public class SubTaskCtrl implements Component<SubTask>, Initializable {

    private final MainCtrl mainCtrl;
    private final ServerUtils server;
    private final ClientUtils client;

    @Getter
    private SubTask subTask;

    @FXML
    private VBox subTaskView;

    @FXML
    private TextField title;

    @FXML

    private CheckBox checkBox;

    @FXML
    private Button deleteButton;

    @Inject
    public SubTaskCtrl(MainCtrl mainCtrl, ServerUtils server, ClientUtils client) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.client = client;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SubTask.fxml"));
        loader.setController(this);
        loader.setRoot(this);
    }

    @Override
    public void loadData(SubTask subTask) {
        this.subTask = subTask;
        title.setText(subTask.getTitle());
        checkBox.setSelected(subTask.getFinished());
    }

    @Override
    public Parent getNode() {
        return subTaskView;
    }

    public void delete() {
        if (!client.getBoardCtrl().getBoard().isEditable()) {
            throw new IllegalStateException("You do not have permissions to edit this board.");
        }
        this.subTask = server.deleteSubTask(subTask.getId());
    }

    public void checkBoxClicked() {
        subTask.setFinished(!subTask.getFinished());
        server.updateSubTask(subTask);
    }

    public void refresh() {
        loadData(server.getSubTask(subTask.getId()));
    }

    public void remove() {
        client.getSubTaskCtrls().remove(subTask.getId());
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        title.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (title.getText().equals("")) {
                    title.setText(subTask.getTitle());
                    throw new RuntimeException("SubTask title cannot be empty");
                }
                subTask.setTitle(title.getText());
                server.updateSubTask(subTask);
            }
        });
    }
}
