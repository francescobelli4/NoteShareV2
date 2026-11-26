package app.models.explorermodel;

import app.entities.ExplorerItemEntity;
import app.utils.ListenerHelper;
import app.utils.Subscription;

import java.util.ArrayList;
import java.util.List;

/**
 * This model represents the explorer in the app. The explorer should be able to display every type of item, and it
 * should notify its listeners when the items list is updated.
 * The explorer is used to show the library, the shop, etc...
 */
public class ExplorerModel {

    private final ListenerHelper<ExplorerModelListener> listeners = new ListenerHelper<>();

    private final List<ExplorerItemEntity> items = new ArrayList<>();
    private ExplorerMode mode = ExplorerMode.LIBRARY;
    private ExplorerItemEntity selectedItem;

    public Subscription addListener(ExplorerModelListener listener) {
        return listeners.subscribe(listener);
    }

    /**
     * This function should update the items list and notify the listeners
     * @param newItems the new items list
     */
    public void setItems(List<ExplorerItemEntity> newItems) {
        items.clear();
        items.addAll(newItems);

        listeners.triggerEvent(listener -> listener.itemsUpdated(List.copyOf(items)));
    }

    /**
     * This function should set a new working mode for the browser. Setting the proper working mode allows controllers
     * to update the browser's items only when needed.
     * @param mode the new working mode
     */
    public void setMode(ExplorerMode mode) {
        this.mode = mode;
        selectedItem = null;

        listeners.triggerEvent(ExplorerModelListener::onItemDeSelected);
        listeners.triggerEvent(listener -> listener.explorerModeUpdated(mode));
    }

    public void setSelectedItem(ExplorerItemEntity selectedItem) {
        this.selectedItem = selectedItem;
        listeners.triggerEvent(listener -> listener.onItemSelected(selectedItem));
    }

    public void deSelectItem() {
        selectedItem = null;
        listeners.triggerEvent(ExplorerModelListener::onItemDeSelected);
    }

    public ExplorerItemEntity getSelectedItem() {
        return selectedItem;
    }

    public ExplorerMode getMode() {
        return mode;
    }

    public List<ExplorerItemEntity> getItems() {
        return List.copyOf(items);
    }
}
