package client.components;

import commons.DBEntity;

public interface DBEntityCtrl<T, C extends DBEntity> {
    public void loadData(T t);
    public void refresh();
    public void remove();
    public void removeChildren();
    public void replaceChild(C c);
}
