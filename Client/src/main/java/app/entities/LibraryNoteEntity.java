package app.entities;

public final class LibraryNoteEntity extends ExplorerItemEntity {

    private LibraryFolderEntity parentFolder;

    public LibraryNoteEntity(String name) {
        super(name);
    }

    public LibraryFolderEntity getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(LibraryFolderEntity parentFolder) {
        this.parentFolder = parentFolder;
    }
}
