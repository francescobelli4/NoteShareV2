package app.models.explorermodel;

import app.entities.ExplorerItemEntity;
import app.entities.LibraryFolderEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ExplorerModelTest {

    private static final ExplorerModel explorerModel = new ExplorerModel();
    private static ExplorerModelListener explorerModelListener;

    @BeforeAll
    static void beforeAll() {
        explorerModelListener = mock(ExplorerModelListener.class);
        explorerModel.addListener(explorerModelListener);
    }

    @Test
    void setItems() {

        List<ExplorerItemEntity> newItems = new ArrayList<>(List.of(new LibraryFolderEntity("folder1")));

        explorerModel.setItems(newItems);

        assertEquals(1, explorerModel.getItems().size());
        verify(explorerModelListener).itemsUpdated(any());
    }

    @Test
    void setMode() {
        explorerModel.setMode(ExplorerMode.STORE);
        assertEquals(ExplorerMode.STORE, explorerModel.getMode());
        verify(explorerModelListener).explorerModeUpdated(any());
    }
}