package client.components;

import client.scenes.EditCardCtrl;
import client.scenes.MainCtrl;
import client.utils.ClientUtils;
import client.utils.ServerUtils;
import commons.Card;
import commons.Tag;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import lombok.Getter;

import javax.inject.Inject;


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
        label.setText(tag.getText());
        colorString = "-fx-background-color: #" + tag.getColor();
        colorString = colorString.replaceAll("0x", "");
        anchorPane.setStyle(colorString);
    }

    @Override
    public Parent getNode() {
        return anchorPane;
    }


    @FXML
    public void clickAssigning(MouseEvent event){
        if(mainCtrl.getActiveCtrl().getClass() == EditCardCtrl.class){
            EditCardCtrl editCardCtrl = (EditCardCtrl) mainCtrl.getActiveCtrl();
            Card card = client.getCard(editCardCtrl.getCardId());
            if(isAssigned){
                isAssigned = false;
            } else {
                isAssigned = true;
                assignThisToCard(card);
                System.out.println("Clicked on Tag!");
            }
        }
    }

    public void assignThisToCard(Card card){
        System.out.println("Assigning is called");
        int freePosition = 0;
        for(int i = 0; i < card.getCardTagId().length; i++){
            if(card.getCardTagId()[i] > 0);{
                freePosition = i;
            }
        }
        card.getCardTagText()[freePosition] = tag.getText();
        card.getCardTagId()[freePosition] = (int) tag.getId();
        card.getCardTagColor()[freePosition] = tag.getColor();
        server.updateCard(card);
    }



    public void delete(){

    }

    public void refresh(){

    }


    public void remove() {

    }


    public void removeChildren() {

    }


    public void replaceChild(Tag tag) {

    }
}
