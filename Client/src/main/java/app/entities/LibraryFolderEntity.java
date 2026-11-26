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

    /**
     * This function should remove a folder from subFolders.
     * @param libraryFolderEntity the folder that has to be removed
     */
    public void deleteSubFolder(LibraryFolderEntity libraryFolderEntity) {
        subFolders.remove(libraryFolderEntity);
    }

    /**
     * This function should remove a note from notes.
     * @param libraryNoteEntity the note that has to be removed.
     */
    public void deleteNote(LibraryNoteEntity libraryNoteEntity) {
        notes.remove(libraryNoteEntity);
    }

    /**
     * This function should check if a subfolder with a given name exists in this folder.
     * @param name the name we want to check
     * @return true if a subfolder with that name exists.
     */
    public boolean hasSubFolderNamed(String name) {
        for (LibraryFolderEntity folder : subFolders) {
            if (Objects.equals(folder.getName(), name)) return true;
        }

        return false;
    }

    /**
     * This function should check if a note with a given name exists in this folder.
     * @param name the name we want to check
     * @return true if a note with that name exists.
     */
    public boolean hasNoteNamed(String name) {
        for (LibraryNoteEntity note : notes) {
            if (Objects.equals(note.getName(), name)) return true;
        }

        return false;
    }

    /**
     * This function should check if this folder is a descendant (or is the same) of a specified LibraryFolderEntity.
     * @param folder the folder we want to check
     * @return true if this folder is a descendant of the specified folder.
     */
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
