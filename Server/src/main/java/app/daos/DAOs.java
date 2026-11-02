package app.daos;

import app.daos.user.UserDAO;

import java.util.Objects;

public record DAOs(UserDAO userDAO) {

    public DAOs {
        Objects.requireNonNull(userDAO);
    }
}
