package client.components;

import client.scenes.EditCardCtrl;
import client.scenes.MainCtrl;
import client.utils.ClientUtils;
import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
import commons.Tag;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Popup;
import javafx.stage.Stage;
import lombok.Getter;

import javax.inject.Inject;
import java.io.IOException;


public class TagCtrl implements Component<Tag>{

    private final MainCtrl mainCtrl;

    private final ServerUtils server;

    private final ClientUtils client;

    @Getter
    private Tag tag;

    @FXML
    private Label label;

    @FXML
    private AnchorPane anchorPane;

    public boolean isAssigned = false;

    private String colorString;

    @FXML
    private TextField textField;

    @FXML
    private ColorPicker colorPicker;

    private Color color;

    @FXML
    private Button button;

    @FXML
    private Text savedText;



    @Inject
    public TagCtrl(MainCtrl mainCtrl, ServerUtils server, ClientUtils client) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.client = client;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Tag.fxml"));
        loader.setController(this);
        loader.setRoot(this);

    }

    @Override
    public void loadData(Tag tag) {
        this.tag = tag;
        if(tag == null){
            return;
        }
        String col = "";
        if(tag.getColor() == null){
            col = "FFFFFF";
        }else{
            col = tag.getColor();
        }
        label.setText(tag.getText());
        colorString = "-fx-background-color: #" + col;
        colorString = colorString.replaceAll("0x", "");
        anchorPane.setStyle(colorString);
        if(mainCtrl.getActiveCtrl().getClass() == EditCardCtrl.class){
            if(isAssigned){
                label.setText(label.getText() + "*");
            }
        }
    }

    @Override
    public Parent getNode() {
        return anchorPane;
    }


    @FXML
    public void clickAssigning(MouseEvent event){
        if(mainCtrl.getActiveCtrl().getClass() == EditCardCtrl.class){
            EditCardCtrl editCardCtrl = (EditCardCtrl) mainCtrl.getActiveCtrl();
            Card card1 = client.getCard(editCardCtrl.getCardId());
            if(event.getClickCount() == 2){
                changeTag(card1);
            }
            else if(event.getClickCount() == 1){
                EditCardCtrl editCardCtrl1 = (EditCardCtrl) mainCtrl.getActiveCtrl();
                Card card = client.getCard(editCardCtrl.getCardId());
                if(isAssigned){
                    isAssigned = false;
                    unAssignFromCard(card);
                    System.out.println("UnAssigned Card!");
                } else {
                    isAssigned = true;
                    assignThisToCard(card);
                    System.out.println("Clicked on Tag!");
                }
            }
        }
    }

    public void assignThisToCard(Card card){
        System.out.println("Assigning is called");
        card.getTags().add(tag);
     //   tag.getCards().add(card);
    //    server.updateCard(card);
    }

    public void unAssignFromCard(Card card){
        card.getTags().remove(tag);
     //   tag.getCards().remove(card);
   //     server.updateCard(card);
      //  server.updateTag(tag);
    }

    public void changeTag(Card card){
        mainCtrl.showTagSettings(tag, card);
    }
    public void delete(){
        Board board = client.getBoardCtrl().getBoard();
  //      board.getTagList().remove(tag);
  //      server.updateBoard(board);
        server.deleteTag(tag.getId());
        this.savedText.setText("Deleted Tag");
    }

    public void remove() {

    }


    public void removeChildren() {

    }
    public void refresh(){
        loadData(server.getTag(tag.getId()));
        for(Card card : tag.getCards()){
            client.getCardCtrl(card.getId()).replaceChild(tag);
        }
    }

    public void replaceChild(Tag tag) {

    }
}
