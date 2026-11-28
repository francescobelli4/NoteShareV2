package app.appcontrollers;

import app.entities.ExplorerItemEntity;
import app.models.explorermodel.ExplorerMode;
import app.models.explorermodel.ExplorerModel;

public class ExplorerController {

    private final ExplorerModel explorerModel;

    public ExplorerController(ExplorerModel explorerModel) {
        this.explorerModel = explorerModel;
    }

    /**
     * This function should set a new selectedItem in the explorer model.
     * @param itemEntity the new selected item
     */
    public void itemClicked(ExplorerItemEntity itemEntity) {
        explorerModel.setSelectedItem(itemEntity);
    }

    public void setExplorerMode(ExplorerMode explorerMode) {
        explorerModel.setMode(explorerMode);
    }
}
