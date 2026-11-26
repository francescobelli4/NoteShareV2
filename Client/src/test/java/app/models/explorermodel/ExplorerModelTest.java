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

    @BeforeEach
    void resetInvocations() {
        clearInvocations(explorerModelListener);
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

        assertNull(explorerModel.getSelectedItem());
        assertEquals(ExplorerMode.STORE, explorerModel.getMode());

        verify(explorerModelListener).onItemDeSelected();
        verify(explorerModelListener).explorerModeUpdated(any());
    }

    @Test
    void setSelectedItemTest() {

        ExplorerItemEntity itemEntity = new LibraryFolderEntity("name");
        explorerModel.setSelectedItem(itemEntity);

        assertEquals(itemEntity, explorerModel.getSelectedItem());
        verify(explorerModelListener).onItemSelected(itemEntity);
    }

    @Test
    void deSelectItemTest() {

        ExplorerItemEntity itemEntity = new LibraryFolderEntity("name");
        explorerModel.setSelectedItem(itemEntity);

        explorerModel.deSelectItem();

        assertNull(explorerModel.getSelectedItem());
        verify(explorerModelListener).onItemDeSelected();
    }
}