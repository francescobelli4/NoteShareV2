package app.exceptions;

import utils.LibraryError;

public class LibraryException extends RuntimeException {

    private final transient LibraryError libraryError;
    public LibraryException(LibraryError libraryError) {
        super(libraryError.toString());
        this.libraryError = libraryError;
    }

    public LibraryError getLibraryError() {
        return libraryError;
    }
}
