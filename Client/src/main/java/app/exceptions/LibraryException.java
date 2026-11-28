package app.exceptions;

import errors.LibraryError;

public class LibraryException extends RuntimeException {

    private final transient LibraryError libraryError;
    private final String causingItemName;

    public LibraryException(LibraryError libraryError) {
        this(libraryError, null);
    }

    public LibraryException(LibraryError libraryError, String causingItemName) {
        super(libraryError.toString());

        this.libraryError = libraryError;
        this.causingItemName = causingItemName;
    }

    public LibraryError getLibraryError() {
        return libraryError;
    }

    public String getCausingItemName() {
        return causingItemName;
    }
}
