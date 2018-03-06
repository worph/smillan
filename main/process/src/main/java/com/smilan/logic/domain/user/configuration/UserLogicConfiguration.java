package com.smilan.logic.domain.user.configuration;

import com.google.common.base.Function;
import com.smilan.api.domain.user.User;
import com.smilan.logic.domain.security.CrudPermission;
import com.smilan.logic.common.configuration.EntityManagerConfiguration;
import com.smilan.logic.domain.security.Realm;
import com.smilan.logic.domain.security.configuration.RealmConfiguration;
import com.smilan.logic.domain.user.DefaultUserLogic;
import com.smilan.logic.domain.user.UserDAOInterface;
import com.smilan.logic.domain.user.UserLogic;
import com.smilan.logic.domain.user.builder.JPAUserDAOBuilder;
import com.smilan.logic.domain.user.entity.UserEntity;
import com.smilan.logic.domain.user.function.UserEntityToUserFunction;
import com.smilan.logic.domain.user.function.UserToUserEntityFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Thomas
 *
 */
@Configuration
public class UserLogicConfiguration {

    @Bean
    public UserDAOInterface userDAO(EntityManagerConfiguration entityManagerConfiguration) {
        final CrudPermission crudPermission = new CrudPermission("user");
        return new JPAUserDAOBuilder()
                .withEntityManager(entityManagerConfiguration.getEntityManager())
                .withCrudPermission(crudPermission)
                .build();
    }

    @Bean
    public UserLogic userLogic(
            UserDAOInterface userDAO,
            Function<User, UserEntity> userToUserEntityFunction,
            Function<UserEntity, User> userEntityToUserFunction) {
        return new DefaultUserLogic(userDAO, userToUserEntityFunction, userEntityToUserFunction);
    }

    @Bean
    public Function<User, UserEntity> userToUserEntityFunction() {
        UserToUserEntityFunction userToUserEntityFunction = new UserToUserEntityFunction();
        return userToUserEntityFunction;
    }

    @Bean
    public Function<UserEntity, User> userEntityToUserFunction() {
        UserEntityToUserFunction userEntityToUserFunction = new UserEntityToUserFunction();
        return userEntityToUserFunction;
    }

}
