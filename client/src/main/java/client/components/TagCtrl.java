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


public class TagCtrl implements Component<Tag>, DBEntityCtrl<Tag, Tag> {

    private final MainCtrl mainCtrl;

    private final ServerUtils server;

    private ClientUtils client;

    @Getter
    private Tag tag;

    @FXML
    private Label label;

    @FXML
    private Color color;

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
        if(tag == null || label == null){
            return;
        }
        label.setText(tag.getText());
        color = Color.rgb(tag.getRed(), tag.getBlue(),
                tag.getGreen());
        String string = "-fx-background-color: #" + color;
        string = string.replaceAll("0x", "");
        anchorPane.setStyle(string);
    }

    @Override
    public Parent getNode() {
        return label.getParent();
    }

    public void editTag() {
        server.updateTag(this.tag);
    }

    public void assignTagToCard(long cardId){
        Card card = client.getCard(cardId);
        card.getTags().add(tag);
        server.updateCard(card);

        tag.getCards().add(card);
        server.getCard(cardId).getTags().add(tag);
        server.getTag(tag.getId()).getCards().add(card);
        server.updateTag(tag);
    }

    public void delete(){
        server.deleteTag(tag.getId());
        //TODO delete the FXML thingy that you see of the tag
    }

    public void refresh(){
        loadData(server.getTag(tag.getId()));
        for(Card card : tag.getCards()){
            client.getCardCtrl(card.getId()).replaceChild(tag);
        }
    }

    @Override
    public void remove() {
        client.getTagCtrls().remove(tag.getId());
        removeChildren();
    }

    @Override
    public void removeChildren() {

    }

    @Override
    public void replaceChild(Tag tag) {

    }
}
