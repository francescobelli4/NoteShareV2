package app.appcontrollers;

import app.entities.ExplorerItemEntity;
import app.models.explorermodel.ExplorerModel;

public class ExplorerController {

    private final ExplorerModel explorerModel;

    public ExplorerController(ExplorerModel explorerModel) {
        this.explorerModel = explorerModel;
    }

    public void itemClicked(ExplorerItemEntity itemEntity) {
        explorerModel.setSelectedItem(itemEntity);
    }
}
