package client.components;

import client.scenes.EditCardCtrl;
import client.scenes.MainCtrl;
import client.utils.ClientUtils;
import client.utils.ServerUtils;
import commons.Board;
import commons.Card;
import commons.CardTag;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import lombok.Getter;

import javax.inject.Inject;


public class TagCtrl implements Component<Tag> {

    private final MainCtrl mainCtrl;

    private final ServerUtils server;

    private final ClientUtils client;

    @Getter
    private Tag tag;

    @FXML
    private Label label;

    @FXML
    private AnchorPane anchorPane;

    private boolean isAssigned = false;

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

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }

    @Override
    public void loadData(Tag tag) {
        this.tag = tag;
        if (tag == null) {
            return;
        }
        String col = "";
        if (tag.getColor() == null) {
            col = "FFFFFF";
        } else {
            col = tag.getColor();
        }
        label.setText(tag.getText());
        colorString = "-fx-background-color: #" + col;
        colorString = colorString.replaceAll("0x", "");
        anchorPane.setStyle(colorString);
        if (mainCtrl.getActiveCtrl().getClass() == EditCardCtrl.class) {
            if (isAssigned) {
                this.label.setFont(Font.font(label.getFont().getFamily(),
                        FontWeight.EXTRA_BOLD, label.getFont().getSize()));
            } else {
                this.label.setFont(Font.font(label.getFont().getFamily(),
                        FontWeight.NORMAL, label.getFont().getSize()));
            }
        }
    }

    @Override
    public Parent getNode() {
        return anchorPane;
    }


    @FXML
    public void clickAssigning(MouseEvent event) {
        if (mainCtrl.getActiveCtrl().getClass() == EditCardCtrl.class) {
            EditCardCtrl editCardCtrl = (EditCardCtrl) mainCtrl.getActiveCtrl();
            Card card1 = client.getCard(editCardCtrl.getCardId());
            if (event.getButton() == MouseButton.SECONDARY) {
                changeTag(card1);
            } else if (event.getButton() == MouseButton.PRIMARY) {
                EditCardCtrl editCardCtrl1 = (EditCardCtrl) mainCtrl.getActiveCtrl();
                Card card = client.getCard(editCardCtrl.getCardId());
                if (isAssigned) {
                    this.label.setFont(Font.font(label.getFont().getFamily(),
                            FontWeight.NORMAL, label.getFont().getSize()));
                    isAssigned = false;
                    unAssignFromCard(card);
                    System.out.println("UnAssigned Card!");
                } else {
                    isAssigned = true;
                    this.label.setFont(Font.font(label.getFont().getFamily(),
                            FontWeight.EXTRA_BOLD, label.getFont().getSize()));
                    assignThisToCard(card);
                    System.out.println("Clicked on Tag!");
                }
            }
        }
    }

    public void assignThisToCard(Card card) {
        if (mainCtrl.getActiveCtrl().getClass() == EditCardCtrl.class) {
            EditCardCtrl editCardCtrl = (EditCardCtrl) mainCtrl.getActiveCtrl();
            editCardCtrl.getTagAssignText().setVisible(true);
            editCardCtrl.getTagAssignText().setText("Assigned Tag to the Card!");
        }

        System.out.println("Assigning is called");
        CardTag cardTag = new CardTag(card, tag);
        System.out.println("card set to cardTag: " + card.getTitle());
        System.out.println("tag set to cardTag: " + tag.getText());
        server.createCardTag(cardTag, client.getBoardCtrl().getBoard().getId());
    }

    public void unAssignFromCard(Card card) {
        if (mainCtrl.getActiveCtrl().getClass() == EditCardCtrl.class) {
            EditCardCtrl editCardCtrl = (EditCardCtrl) mainCtrl.getActiveCtrl();
            editCardCtrl.getTagAssignText().setVisible(true);
            editCardCtrl.getTagAssignText().setText("Unassigned Tag from the Card!");
        }
        for (CardTag cardTag : server.getCardTags()) {
            if (cardTag.getCard().equals(card) && cardTag.getTag().equals(tag)) {
                server.deleteCardTag(cardTag.getId());
            }
        }
    }

    public void changeTag(Card card) {
        mainCtrl.showTagSettings(tag, card);
    }

    public void delete() {
        Board board = client.getBoardCtrl().getBoard();
        server.deleteTag(tag.getId());
        this.savedText.setText("Deleted Tag");
        if (mainCtrl.getActiveCtrl().getClass() == EditCardCtrl.class) {
            EditCardCtrl editCardCtrl = (EditCardCtrl) mainCtrl.getActiveCtrl();
            editCardCtrl.loadData(editCardCtrl.getCardId());
        }
    }

    public void remove() {

    }


    public void removeChildren() {
    }

    public void refresh(Card card) {
        loadData(server.getTag(tag.getId()));
        for (CardTag cardTag : server.getCardTags()) {
            if(cardTag.getTag().equals(tag) && cardTag.getCard().equals(card)){
                System.out.println("refreshed tags");
                client.getCardCtrl(card.getId()).refresh();
            }
        }
    }
    public void replaceChild(Tag tag) {
    }
}
