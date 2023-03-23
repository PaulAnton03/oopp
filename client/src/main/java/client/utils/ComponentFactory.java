package client.utils;

import client.components.*;
import java.util.Map;

import static client.Main.FXML;

public class ComponentFactory {
    private final Map<Class<? extends Component<?>>, String> fxmlFileName = Map.of(
            BoardCtrl.class, "Board.fxml",
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
    public <T extends Component<D>, D> T create(Class<T> ctrlClass, D data) {
        var pair = FXML.load(ctrlClass, "client", "components", fxmlFileName.get(ctrlClass));
        T ctrl = pair.getKey();
        ctrl.loadData(data);
        return ctrl;
    }
}