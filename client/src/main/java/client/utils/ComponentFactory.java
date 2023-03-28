package client.utils;

import client.components.*;
import commons.DBEntity;
import javafx.util.Pair;

import java.util.Map;
import java.util.function.Supplier;

import com.google.inject.Inject;

import static client.Main.FXML;

public class ComponentFactory {
    private ClientUtils clientUtils;

    @Inject
    public ComponentFactory(ClientUtils clientUtils) {
        this.clientUtils = clientUtils;
    }

    private Map<Class<? extends Component<?>>, Pair<String, Supplier<Map<Long, DBEntity>>> fxmlFileName = Map.of(
        BoardCtrl.class, new Pair<>("Board.fxml", () -> clientUtils.getCardListCtrls()),
            CardListCtrl.class, "CardList.fxml",
            CardCtrl.class, "Card.fxml",
            BoardJoinCtrl.class, "BoardJoin.fxml"
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
     * @param ctrlClass controller class for this component
     * @param data data to populate the component with
     * @param <T> controller type
     * @param <D> data type
     * @return controller for the new component
     */
    public <T extends Component<DBEntity>, DBEntity> T create(Class<T> ctrlClass, DBEntity data) {
        var pair = FXML.load(ctrlClass, "client", "components", fxmlFileName.get(ctrlClass));
        T ctrl = pair.getKey();
        ctrl.loadData(data);
        return ctrl;
    }
}
