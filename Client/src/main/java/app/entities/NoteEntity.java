package app.entities;

public class NoteEntity extends ExplorerItemEntity {

    private FolderEntity parentFolder;

    public NoteEntity(String name) {
        super(ExplorerItemType.NOTE, name);
    }

    public FolderEntity getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(FolderEntity parentFolder) {
        this.parentFolder = parentFolder;
    }
}
