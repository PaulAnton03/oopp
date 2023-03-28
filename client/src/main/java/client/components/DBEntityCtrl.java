package client.components;

public interface DBEntityCtrl<T> {
    public void loadData(T t);
    public void refresh();
    public void remove();
    public void removeChildren();
}
