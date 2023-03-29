package client.components;

import client.scenes.MainCtrl;
import client.utils.ClientUtils;
import client.utils.ServerUtils;
import commons.DBEntity;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import lombok.Getter;

import javax.inject.Inject;


public class TagCtrl implements Component<Tag>, DBEntityCtrl<Tag, Tag> {

    private final MainCtrl mainCtrl;

    private final ServerUtils serverUtils;

    @Getter
    private Tag tag;

    @FXML
    private Label label;

    @FXML
    private Color color;

    ClientUtils client;
    @Inject
    public TagCtrl(MainCtrl mainCtrl, ServerUtils serverUtils, ClientUtils client) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = serverUtils;
        this.client = client;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Tag.fxml"));
        loader.setController(this);
        loader.setRoot(this);
    }

    @Override
    public void loadData(Tag tag) {
        this.tag = tag;
        label.setText(tag.getText());
        color = Color.rgb(tag.getColor().getRed(), tag.getColor().getBlue(),
                tag.getColor().getGreen());
    }

    @Override
    public Parent getNode() {
        return label.getParent();
    }

    public void editTag() {
        //TODO
    }

    public void delete(){
        serverUtils.deleteTag(tag.getId());
        //TODO delete the FXML thingy that you see of the tag
    }

    public void refresh(){
        loadData(serverUtils.getTag(tag.getId()));
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
