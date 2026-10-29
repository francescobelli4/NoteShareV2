package app.services;

import app.daos.user.UserDAO;
import app.entities.UserEntity;
import app.exceptions.AuthServiceException;
import dtos.LoginRequestDTO;
import dtos.RegisterRequestDTO;
import utils.AuthRules;
import utils.Hashing;
import utils.PasswordError;
import utils.UsernameError;

import java.util.Optional;

public class AuthService {

    private final UserDAO userDAO;

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void performRegister(RegisterRequestDTO registerRequestDTO) throws AuthServiceException {

        Optional<UsernameError> usernameError = AuthRules.isUsernameValid(registerRequestDTO.username());
        if (usernameError.isPresent()) {
            String error = switch (usernameError.get()) {
                case NOT_ALPHANUMERIC -> "not_alphanumeric";
                case TOO_LONG -> "too_long";
                case NULL_OR_EMPTY -> "null_or_empty";
                case TOO_SHORT -> "too_short";
            };

            throw new AuthServiceException(error);
        }

        Optional<PasswordError> passwordError = AuthRules.isPasswordValid(registerRequestDTO.password());

        UserEntity entity = new UserEntity(
                registerRequestDTO.username(),
                Hashing.hashString(registerRequestDTO.password()),
                registerRequestDTO.role(),
                ""
                );

        if (!userDAO.saveUser(entity)) {

        }
    }

    public void performLogin(LoginRequestDTO loginRequestDTO) {

    }
}
