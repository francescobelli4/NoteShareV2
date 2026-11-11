package app.entities;

import java.util.ArrayList;
import java.util.List;

public class FolderEntity {
    private FolderEntity parentFolder;
    private final List<FolderEntity> subFolders = new ArrayList<>();
    private final List<NoteEntity> notes = new ArrayList<>();
    private final String name;

    public FolderEntity(String name) {
        this.name = name;
    }

    public void addSubFolder(FolderEntity folderEntity) {
        folderEntity.parentFolder = this;
        subFolders.add(folderEntity);
    }

    public FolderEntity getParentFolder() {
        return parentFolder;
    }

    public List<FolderEntity> getSubFolders() {
        return subFolders;
    }

    public List<NoteEntity> getNotes() {
        return notes;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        if (parentFolder == null) return getName() + "/";
        return parentFolder.getPath()  + getName();
    }
}
