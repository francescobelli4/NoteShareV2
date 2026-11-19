package app.entities;

/**
 * This class represents an Item in the explorer. Every item should extend this class, this allows to have different
 * items that can be displayed in the explorer and every item type has its own logic.
 */
public abstract sealed class ExplorerItemEntity permits LibraryFolderEntity, LibraryNoteEntity {
    protected String name;

    protected ExplorerItemEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
