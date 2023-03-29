package client.components;

import commons.DBEntity;

/**
 * An interface that must be inherited by all Ctrls responsible for a DBEntity
 * T is the DBEntity type controlled, while C is the type of the child of that DBEntity
 */
public interface DBEntityCtrl<T extends DBEntity, C extends DBEntity> {
    /**
     * Sets own data based on passed object
     * @param t DBEntity
     */
    public void loadData(T t);

    /**
     * Refreshes own DBEntity and children with data from server
     */
    public void refresh();

    /**
     * Removes self and all children only from client
     */
    public void remove();

    /**
     * Removes all children only from client
     */
    public void removeChildren();

    /**
     * Replace a child with a new DBEntity with the same ID
     * @param c Child
     */
    public void replaceChild(C c);
}
