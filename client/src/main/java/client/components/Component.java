package client.components;

import javafx.scene.Parent;

public interface Component <T> {
    void loadData(T data);
    Parent getScene();
}
