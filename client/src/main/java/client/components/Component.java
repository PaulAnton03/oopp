package client.components;

import javafx.scene.Parent;

public interface Component <T> {
    /**
     * Populates this controller with a data object
     * @param data the data object
     */
    void loadData(T data);

    /**
     * Gets the javafx node that this controller has power over
     * @return javafx node
     */
    Parent getNode();
}
