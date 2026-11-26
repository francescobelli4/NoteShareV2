package app.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class LibraryFolderEntity extends ExplorerItemEntity {
    private LibraryFolderEntity parentFolder;
    private final List<LibraryFolderEntity> subFolders = new ArrayList<>();
    private final List<LibraryNoteEntity> notes = new ArrayList<>();

    public LibraryFolderEntity(String name) {
        super(name);
    }

    /**
     * This function should add a new subfolder to this folder.
     * @param libraryFolderEntity the new subfolder
     */
    public void addSubFolder(LibraryFolderEntity libraryFolderEntity) {
        libraryFolderEntity.parentFolder = this;
        subFolders.add(libraryFolderEntity);
    }

    /**
     * This function should add a new note to this folder.
     * @param libraryNoteEntity the new note
     */
    public void addNote(LibraryNoteEntity libraryNoteEntity) {
        libraryNoteEntity.setParentFolder(this);
        notes.add(libraryNoteEntity);
    }

    public void deleteSubFolder(LibraryFolderEntity libraryFolderEntity) {
        subFolders.remove(libraryFolderEntity);
    }

    public void deleteNote(LibraryNoteEntity libraryNoteEntity) {
        notes.remove(libraryNoteEntity);
    }

    public boolean hasSubFolderNamed(String name) {
        for (LibraryFolderEntity folder : subFolders) {
            if (Objects.equals(folder.getName(), name)) return true;
        }

        return false;
    }

    public boolean hasNoteNamed(String name) {
        for (LibraryNoteEntity note : notes) {
            if (Objects.equals(note.getName(), name)) return true;
        }

        return false;
    }

    public boolean isDescendantOf(LibraryFolderEntity folder) {
        LibraryFolderEntity temp = this;

        while (temp != null) {
            if (temp == folder) return true;
            temp = temp.getParentFolder();
        }

        return false;
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
