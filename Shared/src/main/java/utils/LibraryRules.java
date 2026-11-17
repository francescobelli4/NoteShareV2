package utils;

import java.util.List;
import java.util.Optional;

public class LibraryRules {
    public static final int NAME_MAX = 20;

    private LibraryRules() {
        // Nothing to do :D
    }

    public static Optional<LibraryError> isNameValid(String name) {
        if (name == null) return Optional.of(LibraryError.NAME_EMPTY);

        int length = name.length();

        if (length == 0) return Optional.of(LibraryError.NAME_EMPTY);

        if (length > NAME_MAX) return Optional.of(LibraryError.NAME_TOO_LONG);

        if (!Utils.isAlphanumeric(name)) return Optional.of(LibraryError.NAME_NOT_VALID);

        return Optional.empty();
    }

    public static Optional<LibraryError> isDuplicate(String name, List<String> siblings) {

        if (siblings.contains(name)) return Optional.of(LibraryError.FOLDER_ALREADY_EXISTS);
        return Optional.empty();
    }
}
