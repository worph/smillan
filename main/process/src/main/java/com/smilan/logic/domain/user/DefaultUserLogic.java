package com.smilan.logic.domain.user;

import com.google.common.base.Function;
import com.smilan.api.domain.user.User;
import com.smilan.api.domain.user.UserManagerDTO;
import com.smilan.api.domain.user.builder.UserManagerDTOBuilder;
import com.smilan.logic.domain.security.CrudPermission;
import com.smilan.logic.common.DefaultEntityLogic;
import com.smilan.logic.common.EntityDAO;
import com.smilan.logic.domain.security.Realm;
import com.smilan.logic.domain.user.entity.UserEntity;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Worph
 */
public class DefaultUserLogic extends DefaultEntityLogic<UserEntity, User, UserManagerDTO> implements UserLogic {

    private final UserDAOInterface userDAOInterface;
    private final Function<UserEntity, User> userEntityToUserFunction;
    private final Function<User, UserEntity> userToUserEntityFunction;

    public DefaultUserLogic(
            UserDAOInterface announceDAO,
            Function<User, UserEntity> userToUserEntityFunction,
            Function<UserEntity, User> userEntityToUserFunction) {
        super(announceDAO, userToUserEntityFunction, userEntityToUserFunction);
        userDAOInterface = announceDAO;
        this.userEntityToUserFunction = userEntityToUserFunction;
        this.userToUserEntityFunction = userToUserEntityFunction;
    }

    @Override
    public UserManagerDTO buildEntityManagerDTO(List<User> announces) {
        return new UserManagerDTOBuilder().withEntities(announces).build();
    }

    @Override
    public User getUserFromUsername(String username) {
        final UserEntity userFromUsername = userDAOInterface.getUserFromUsername(username);
        if (userFromUsername == null) {
            return null;
        } else {
            return userEntityToUserFunction.apply(userFromUsername);
        }
    }
    
    @Override
    public User getUserFromUserId(String userId) {
        final UserEntity userFromUsername = userDAOInterface.getUserFromUserId(userId);
        if (userFromUsername == null) {
            return null;
        } else {
            return userEntityToUserFunction.apply(userFromUsername);
        }
    }
    
    @Override
    public User getUserFromApiKey(String apiKey) {
        final UserEntity userFromUsername = userDAOInterface.getUserFromApiKey(apiKey);
        if (userFromUsername == null) {
            return null;
        } else {
            return userEntityToUserFunction.apply(userFromUsername);
        }
    }

    @Override
    public void setLogin(String id, String login) {
        userDAOInterface.setLogin(Long.parseLong(id), login);
    }

    @Override
    public void setPassword(String id, String password) {
        userDAOInterface.setPassword(Long.parseLong(id), password);
    }

    @Override
    public void securityFilterData(User entity, CrudPermission crudPermission) {
        long id = Long.parseLong(entity.getId());
        if (!crudPermission.hasReadPermission(id, UserEntity.NAME_EMAIL)) {
            entity.setEmail(null);
        }
        if (!crudPermission.hasReadPermission(id, UserEntity.NAME_APIKEY)) {
            entity.setApiKey(null);
        }
        if (!crudPermission.hasReadPermission(id, UserEntity.NAME_LOGIN)) {
            entity.setLogin(null);
        }
        if (!crudPermission.hasReadPermission(id, UserEntity.NAME_PASSWORD)) {
            entity.setPassword(null);
        }
        if (!crudPermission.hasReadPermission(id, UserEntity.NAME_XMPPPASSWORD)) {
            entity.setXmppPassword(null);
        }
        if (!crudPermission.hasReadPermission(id, UserEntity.NAME_ROLES)) {
            entity.setRoles(null);
        }
        if (!crudPermission.hasReadPermission(id, UserEntity.NAME_PERMISSION)) {
            entity.setPermissions(null);
        }
    }

    @Override
    public User set(User user) {
        UserEntity entity = userDAOInterface.setUser(userToUserEntityFunction.apply(user));
        if (entity == null) {
            return null;
        } else {
            return userEntityToUserFunction.apply(entity);
        }
    }

}
