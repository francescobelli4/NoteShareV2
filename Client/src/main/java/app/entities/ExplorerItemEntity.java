package app.entities;

public abstract sealed class ExplorerItemEntity permits LibraryFolderEntity, LibraryNoteEntity {
    protected String name;

    protected ExplorerItemEntity(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
