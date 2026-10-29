package app.daos.user;

import app.entities.UserEntity;

import java.util.*;

public class DemoUserDAO implements UserDAO {

    private final Map<String, UserEntity> users = new HashMap<>();

    /**
     * This function should only add a new UserEntity to users because this DAO is used for the demo version of the app.
     * If a user with the specified username already exists, it should return false.
     * @param userEntity the user that has to be saved
     */
    @Override
    public boolean saveUser(UserEntity userEntity) {
        return users.putIfAbsent(userEntity.username(), userEntity) == null;
    }

    /**
     * This function should find a user in users by his username.
     * @param username the user's username
     * @return a UserEntity instance or null
     */
    @Override
    public UserEntity findUserByUsername(String username) {
        return users.get(username);
    }

    /**
     * This function should find a user in users by his token.
     * @param token the user's token
     * @return a UserEntity instance or null
     */
    @Override
    public UserEntity findUserByToken(String token) {
        
        for (UserEntity userEntity : users.values()) {
            if (Objects.equals(userEntity.token(), token)) return userEntity;
        }
        return null;
    }

    /**
     * This function should replace the old UserEntity with a new one with the updated token.
     * @param userEntity the entity that has to be updated
     * @param token the new token
     * @return the new updated entity
     *
     */
    @Override
    public UserEntity updateUserToken(UserEntity userEntity, String token) {
        UserEntity updatedUserEntity = userEntity.updateToken(token);
        users.put(userEntity.username(), updatedUserEntity);
        return updatedUserEntity;
    }
}
