package app.daos.user;

import app.entities.UserEntity;

public interface UserDAO {

    boolean saveUser(UserEntity userEntity);
    UserEntity findUserByUsername(String username);
    UserEntity findUserByToken(String token);

    UserEntity updateUserToken(UserEntity userEntity, String token);
}
