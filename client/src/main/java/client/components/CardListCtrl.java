package client.components;

import client.scenes.MainCtrl;
import client.utils.ClientUtils;
import client.utils.ComponentFactory;
import client.utils.Logger;
import client.utils.ServerUtils;
import commons.Card;
import commons.CardList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Pair;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.inject.Inject;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;


@EqualsAndHashCode
public class CardListCtrl implements Component<CardList>, Initializable {
    private final ClientUtils client;
    private final ServerUtils server;
    private final MainCtrl mainCtrl;
    private final ComponentFactory factory;

    @Getter
    private CardList cardList;
    @Getter
    private HashMap<Long, CardCtrl> cardCtrls = new HashMap<>();
    @FXML
    private Text title;
    @Getter
    @FXML
    private VBox cardListView;

    @Inject
    public CardListCtrl(ClientUtils client, ServerUtils server, MainCtrl mainCtrl, ComponentFactory factory) {
        this.client = client;
        this.server = server;
        this.mainCtrl = mainCtrl;
        this.factory = factory;
    }

    public Parent getNode() {
        return cardListView.getParent();
    }

    @Override
    public void loadData(CardList cardList) {
        this.cardList = cardList;
        title.setText(cardList.getTitle());

        for (Card card : cardList.getCards()) {
            CardCtrl cardCtrl = factory.create(CardCtrl.class, card);
            cardCtrls.put(cardCtrl.getCard().getId(), cardCtrl);
            cardListView.getChildren().add(cardCtrl.getNode());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cardListView.setOnDragEntered(event -> {
            Logger.log("Entered drag");
        });

        cardListView.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            event.consume();
        });

        cardListView.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            Logger.log("Dropped: " + db.getString());
            client.setActiveCardListCtrl(this);

            // Find the position to insert the card\
            final double eventY = event.getY();
            final List<Node> nodes = cardListView.getChildren();
            Pair<Integer, Double> nearestPositionDistance = new Pair<>(nodes.size(), cardListView.getBoundsInLocal().getMaxY() - eventY);
            boolean isPositionAbove = false;

            for (int i = 0; i < nodes.size(); i++) {
                final Node node = nodes.get(i);
                final double signedDistance = eventY - node.getBoundsInParent().getCenterY();
                if (Math.abs(signedDistance) < nearestPositionDistance.getValue()) {
                    nearestPositionDistance = new Pair<>(i, Math.abs(signedDistance));
                    isPositionAbove = signedDistance > 0;
                }
            }

            ((Node) event.getSource()).setUserData(Math.max(0,
                            nearestPositionDistance.getKey() + (isPositionAbove ? 1 : 0)));
            Logger.log("Position: " + nearestPositionDistance.getKey());
            Logger.log("Distance: " + nearestPositionDistance.getValue());
            Logger.log("Is above? " + isPositionAbove);
            event.setDropCompleted(true);
            event.consume();
        });
    }

    public void refresh() {
        cardListView.getChildren().clear();
        try {
            loadData(server.getCardList(cardList.getId()));
        } catch (Exception e) {
            Logger.log("Card list couldn't be refreshed");
        }

    }

    public void addCard() {
        client.setActiveCardListCtrl(this);
        mainCtrl.showAddCard();
    }

    public void listSettings() {
        client.setActiveCardListCtrl(this);
        mainCtrl.showListSettings();
    }
}
