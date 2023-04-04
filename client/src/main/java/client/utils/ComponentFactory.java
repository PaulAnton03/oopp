package client.utils;

import static client.Main.FXML;

import java.util.Map;

import client.components.*;
import com.google.inject.Inject;

import commons.DBEntity;

public class ComponentFactory {
    private ClientUtils client;

    @Inject
    public ComponentFactory(ClientUtils client) {
        this.client = client;
    }

    private Map<Class<? extends Component<?>>, String> fxmlFileName = Map.of(
            BoardCtrl.class, "Board.fxml",
            CardListCtrl.class, "CardList.fxml",
            CardCtrl.class, "Card.fxml",
            BoardJoinCtrl.class, "BoardJoin.fxml",
            SubTaskCtrl.class, "SubTask.fxml"
    );

    /**
     * Method for creating new components populated with data.
     * <pre>
     * Example usage:
     *   {@code
     *   Card card = new Card("title", "description");
     *   CardCtrl cardCtrl = factory.create(CardCtrl.class, card);
     *   }
     * </pre>
     *
     * @param ctrlClass controller class for this component
     * @param data      data to populate the component with
     * @param <T>       controller type
     * @param <D>       data type
     * @return controller for the new component
     */
    public <T extends Component<D>, D extends DBEntity> T create(Class<T> ctrlClass, D data) {
        var pair = FXML.load(ctrlClass, "client", "components", fxmlFileName.get(ctrlClass));
        T ctrl = pair.getKey();

        if (ctrlClass == CardCtrl.class) {
            client.getCardCtrls().put(data.getId(), (CardCtrl) ctrl);
        } else if (ctrlClass == CardListCtrl.class) {
            client.getCardListCtrls().put(data.getId(), (CardListCtrl) ctrl);
        } else if (ctrlClass == SubTaskCtrl.class) {
            client.getSubTaskCtrls().put(data.getId(), (SubTaskCtrl) ctrl);
        }

        ctrl.loadData(data);
        return ctrl;
    }
}
