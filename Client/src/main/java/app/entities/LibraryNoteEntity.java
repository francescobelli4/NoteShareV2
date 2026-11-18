package app.entities;

public class LibraryNoteEntity extends ExplorerItemEntity {

    private LibraryFolderEntity parentFolder;

    public LibraryNoteEntity(String name) {
        super(ExplorerItemType.NOTE, name);
    }

    public LibraryFolderEntity getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(LibraryFolderEntity parentFolder) {
        this.parentFolder = parentFolder;
    }
}
