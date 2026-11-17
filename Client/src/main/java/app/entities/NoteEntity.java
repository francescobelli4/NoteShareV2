package app.entities;

public class NoteEntity {

    private FolderEntity parentFolder;
    private final String name;

    public NoteEntity(String name) {
        this.name = name;
    }

    public FolderEntity getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(FolderEntity parentFolder) {
        this.parentFolder = parentFolder;
    }

    public String getName() {
        return name;
    }
}
