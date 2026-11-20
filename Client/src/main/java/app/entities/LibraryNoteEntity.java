package app.entities;

public final class LibraryNoteEntity extends ExplorerItemEntity {

    private LibraryFolderEntity parentFolder;
    private final String path;

    public LibraryNoteEntity(String name, String path) {
        super(name);
        this.path = path;
    }

    public LibraryFolderEntity getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(LibraryFolderEntity parentFolder) {
        this.parentFolder = parentFolder;
    }

    public String getPath() {
        return path;
    }
}
