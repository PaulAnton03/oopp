package client.components;

import client.scenes.MainCtrl;
import client.utils.ClientUtils;
import client.utils.ServerUtils;
import commons.Card;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import lombok.Getter;

import javax.inject.Inject;


public class TagCtrl implements Component<Tag>{

    private final MainCtrl mainCtrl;

    private final ServerUtils server;

    private ClientUtils client;

    @Getter
    private Tag tag;

    @FXML
    private Label label;

    @FXML
    private AnchorPane anchorPane;


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
        String string = "-fx-background-color: #" + tag.getColor();
        string = string.replaceAll("0x", "");
        anchorPane.setStyle(string);
    }

    @Override
    public Parent getNode() {
        return anchorPane;
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
