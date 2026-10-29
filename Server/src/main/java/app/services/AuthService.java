package app.services;

import app.daos.user.UserDAO;
import app.entities.UserEntity;
import app.exceptions.AuthServiceException;
import dtos.*;
import permissions.Role;
import utils.*;

import java.util.Optional;

public class AuthService {

    private final UserDAO userDAO;

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * This function should actually register a new user. It's reasonable that this function also checks again the
     * username, password and the role.
     * This function also creates the UserEntity that will be saved by the UserDAO.
     * @param registerRequestDTO the received DTO
     * @return the response that will be sent to the client. It will be a RegisterResponseDTO in case of success.
     *         In case of failure, a AuthServiceException(AuthServiceError) is thrown and the MessageHandler should
     *         manage it by creating a ErrorResponseDTO that will be sent to the client.
     * @throws AuthServiceException an exception that specifies the failure cause.
     */
    public Response performRegister(RegisterRequestDTO registerRequestDTO) throws AuthServiceException {

        Optional<UsernameError> usernameError = AuthRules.isUsernameValid(registerRequestDTO.username());
        if (usernameError.isPresent()) {
            throw new AuthServiceException(usernameError.get());
        }

        Optional<PasswordError> passwordError = AuthRules.isPasswordValid(registerRequestDTO.password());
        if (passwordError.isPresent()) {
            throw new AuthServiceException(passwordError.get());
        }

        if (registerRequestDTO.role() == Role.MODERATOR) {
            throw new AuthServiceException(AuthServiceError.INVALID_ROLE);
        }

        UserEntity entity = new UserEntity(
                registerRequestDTO.username(),
                Hashing.hashString(registerRequestDTO.password()),
                registerRequestDTO.role(),
                ""
                );

        if (!userDAO.saveUser(entity)) {
            throw new AuthServiceException(AuthServiceError.USERNAME_TAKEN);
        }

        return new RegisterResponseDTO(registerRequestDTO.requestId(), registerRequestDTO.username(), registerRequestDTO.role());
    }

    /**
     * This function should find a user that matches the credentials in LoginRequestDTO.
     * In case of wrong credentials, AuthServiceException is launched.
     * In case of successful login, it returns a LoginResponseDTO to notify the user about the successful login.
     * @param loginRequestDTO the received dto
     * @return a response that has to be sent to the user
     * @throws AuthServiceException an exception that specifies the failure cause.
     */
    public Response performLogin(LoginRequestDTO loginRequestDTO) throws AuthServiceException {

        UserEntity foundUser = userDAO.findUserByUsername(loginRequestDTO.username());

        if (foundUser == null) {
            throw new AuthServiceException(AuthServiceError.USER_NOT_FOUND);
        }

        if (!Hashing.verifyHash(loginRequestDTO.password(), foundUser.hashedPassword())) {
            throw new AuthServiceException(AuthServiceError.WRONG_PASSWORD);
        }

        return new LoginResponseDTO(loginRequestDTO.requestId(), loginRequestDTO.username(), foundUser.role());
    }
}
