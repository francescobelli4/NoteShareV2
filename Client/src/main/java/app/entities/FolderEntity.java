package app.entities;

import java.util.ArrayList;
import java.util.List;

public class FolderEntity extends ExplorerItemEntity {
    private FolderEntity parentFolder;
    private final List<FolderEntity> subFolders = new ArrayList<>();
    private final List<NoteEntity> notes = new ArrayList<>();

    public FolderEntity(String name) {
        super(ExplorerItemType.FOLDER, name);
    }

    public void addSubFolder(FolderEntity folderEntity) {
        folderEntity.parentFolder = this;
        subFolders.add(folderEntity);
    }

    public void addNote(NoteEntity noteEntity) {
        noteEntity.setParentFolder(this);
        notes.add(noteEntity);
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

    public String getPath() {
        if (parentFolder == null) return getName() + "/";
        return parentFolder.getPath()  + getName();
    }
}
