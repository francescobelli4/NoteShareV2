package app.entities;

import java.util.ArrayList;
import java.util.List;

public class LibraryFolderEntity extends ExplorerItemEntity {
    private LibraryFolderEntity parentFolder;
    private final List<LibraryFolderEntity> subFolders = new ArrayList<>();
    private final List<LibraryNoteEntity> notes = new ArrayList<>();

    public LibraryFolderEntity(String name) {
        super(name);
    }

    public void addSubFolder(LibraryFolderEntity libraryFolderEntity) {
        libraryFolderEntity.parentFolder = this;
        subFolders.add(libraryFolderEntity);
    }

    public void addNote(LibraryNoteEntity libraryNoteEntity) {
        libraryNoteEntity.setParentFolder(this);
        notes.add(libraryNoteEntity);
    }

    public LibraryFolderEntity getParentFolder() {
        return parentFolder;
    }

    public List<LibraryFolderEntity> getSubFolders() {
        return subFolders;
    }

    public List<LibraryNoteEntity> getNotes() {
        return notes;
    }

    public String getPath() {
        if (parentFolder == null) return getName() + "/";
        return parentFolder.getPath()  + getName();
    }
}
