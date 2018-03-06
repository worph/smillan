package com.smilan.logic.domain.user.function;

import com.google.common.base.Function;
import com.smilan.api.domain.user.User;
import com.smilan.logic.domain.user.entity.UserEntity;
import com.smilan.logic.domain.user.entity.builder.UserEntityBuilder;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

public class UserToUserEntityFunction implements Function<User, UserEntity> {

    @Override
    public UserEntity apply(User input) {
        return new UserEntityBuilder()
                .withId(input.getId()==null?null:Long.parseLong(input.getId()))
                .withEmail(input.getEmail())
                .withLogin(input.getLogin())
                .withPassword(input.getPassword())
                .withXmppPassword(input.getXmppPassword())
                .withAnonymous(input.isAnonymous())
                .withApiKey(input.getApiKey())
                .withRoles(input.getRoles())
                .withPermissions(input.getPermissions())
                .build();
    }
}
