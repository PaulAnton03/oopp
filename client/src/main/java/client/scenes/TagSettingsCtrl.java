package client.scenes;

import client.utils.ClientUtils;
import client.utils.ServerUtils;
import commons.Board;
import commons.Tag;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import javax.inject.Inject;

public class TagSettingsCtrl implements SceneCtrl{

    private final MainCtrl mainCtrl;

    private final ServerUtils server;

    private final ClientUtils client;

    private Tag tag;
    @FXML
    private ColorPicker colorPicker;

    private Color color;

    @FXML
    private TextField textField;

    @FXML
    private Button button;

    @FXML
    private Button cancelButton;
    @FXML
    private Text savedText;

    @FXML
    private AnchorPane anchorPane;


    @Inject
    public TagSettingsCtrl(MainCtrl mainCtrl, ServerUtils server, ClientUtils client) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.client = client;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("TagSettings.fxml"));
        loader.setController(this);
        loader.setRoot(this);
    }

    public void loadData(Tag tag){
        this.tag = tag;
    }

    public Parent getNode(){
        return anchorPane;
    }

    public void saveTagChanges() {
        Board board = client.getBoardCtrl().getBoard();
        if(tag == null){
            System.out.println("Could not find tag!");
            this.savedText.setText("Error: Could not find tag!!");
            return;
        }
        if(colorPicker.getValue() != null){
            color = colorPicker.getValue();
            tag.setColor(color.toString());
        }
        if(textField.getText() != null){
            tag.setText(textField.getText());
        }
        if(!board.getTagList().contains(tag))
            board.getTagList().add(tag);
//        else{
//            for(Tag tag1 : board.getTagList()){
//                if(tag1.getId() == tag.getId()){
//
//                }
//            }
//        }
     //   server.updateTag(tag);
        server.updateBoard(board);

        this.savedText.setText("Saved!");
    }

    public void pickColor(ActionEvent event){
        color = colorPicker.getValue();
    }

    public void delete(){
        Board board = client.getBoardCtrl().getBoard();
        board.getTagList().remove(tag);
        server.updateBoard(board);
        server.deleteTag(tag.getId());
        this.savedText.setText("Deleted Tag");
        mainCtrl.showMainView();
    }

    public void resetState(){
        this.savedText.setText("");
        this.textField.setText("");
    }

    public void cancelButtonAction(){
        mainCtrl.showMainView();
    }
}
