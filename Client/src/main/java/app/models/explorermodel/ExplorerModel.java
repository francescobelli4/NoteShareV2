package app.models.explorermodel;

import app.entities.ExplorerItemEntity;
import app.utils.ListenerHelper;
import app.utils.Subscription;

import java.util.ArrayList;
import java.util.List;

public class ExplorerModel {

    private final ListenerHelper<ExplorerModelListener> listeners = new ListenerHelper<>();

    private final List<ExplorerItemEntity> items = new ArrayList<>();
    private ExplorerMode mode = ExplorerMode.LIBRARY;

    public Subscription addListener(ExplorerModelListener listener) {
        return listeners.subscribe(listener);
    }

    public void setItems(List<ExplorerItemEntity> newItems) {
        items.clear();
        items.addAll(newItems);

        listeners.triggerEvent(listener -> listener.itemsUpdated(List.copyOf(items)));
    }

    public void addItem(ExplorerItemEntity item) {
        items.add(item);

        listeners.triggerEvent(listener -> listener.itemsUpdated(List.copyOf(items)));
    }

    public List<ExplorerItemEntity> getItems() {
        return List.copyOf(items);
    }

    public void setMode(ExplorerMode mode) {
        this.mode = mode;
        listeners.triggerEvent(listener -> listener.explorerModeUpdated(mode));
    }

    public ExplorerMode getMode() {
        return mode;
    }
}
