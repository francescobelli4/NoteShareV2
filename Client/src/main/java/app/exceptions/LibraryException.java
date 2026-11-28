package app.exceptions;

import errors.LibraryError;

public class LibraryException extends RuntimeException {

    private final transient LibraryError libraryError;
    private String causingItemName;

    public LibraryException(LibraryError libraryError) {
        super(libraryError.toString());
        this.libraryError = libraryError;
    }

    public LibraryException(LibraryError libraryError, String causingItemName) {
        this(libraryError);
        this.causingItemName = causingItemName;
    }

    public LibraryError getLibraryError() {
        return libraryError;
    }

    public String getCausingItemName() {
        return causingItemName;
    }
}
