package client.components;

import client.scenes.MainCtrl;
import client.utils.ComponentFactory;
import client.utils.ServerUtils;
import commons.Card;
import commons.Tag;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import javax.inject.Inject;

@EqualsAndHashCode
public class CardCtrl implements Component<Card> {
    private final MainCtrl mainCtrl;
    private final ServerUtils serverUtils;
    private final ComponentFactory factory;

    @Getter
    private Card card;
    @FXML
    private VBox cardView;
    @FXML
    private Text title;
    @FXML
    private Text description;

    @FXML
    private FlowPane tagArea;

    @Inject
    public CardCtrl(MainCtrl mainCtrl, ServerUtils serverUtils, ComponentFactory factory) {
        this.mainCtrl = mainCtrl;
        this.serverUtils = serverUtils;
        this.factory = factory;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Card.fxml"));
        loader.setController(this);
        loader.setRoot(this);
    }

    @Override
    public Parent getNode() { return cardView; }

    @Override
    public void loadData(Card card) {
        this.card = card;
        title.setText(card.getTitle());
        description.setText(card.getDescription());

        for (Tag tag : card.getTagList()){
            TagCtrl tagCtrl = factory.create(TagCtrl.class, tag);
            tagArea.getChildren().add(tagCtrl.getNode());
        }
    }

    public void editCard() { mainCtrl.showEditCard(this); }

    public void delete() {
        serverUtils.deleteCard(card.getId());
        ((VBox)cardView.getParent()).getChildren().remove(cardView);
    }

}
