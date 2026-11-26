package app.appcontrollers;

import app.entities.ExplorerItemEntity;
import app.entities.LibraryNoteEntity;
import app.models.explorermodel.ExplorerModel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExplorerControllerTest {

    private static final ExplorerModel explorerModel = new ExplorerModel();
    private static ExplorerController explorerController;

    @BeforeAll
    static void beforeAll() {
        explorerController = new ExplorerController(explorerModel);
    }

    @Test
    void itemClicked() {
        ExplorerItemEntity explorerItemEntity = new LibraryNoteEntity("name", "path");
        explorerController.itemClicked(explorerItemEntity);
        assertEquals(explorerItemEntity, explorerModel.getSelectedItem());
    }
}