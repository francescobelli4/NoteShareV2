package app.models.explorermodel;

import app.entities.ExplorerItemEntity;

import java.util.List;

public interface ExplorerModelListener {
    void itemsUpdated(List<ExplorerItemEntity> newItems);
    void explorerModeUpdated(ExplorerMode mode);
}
