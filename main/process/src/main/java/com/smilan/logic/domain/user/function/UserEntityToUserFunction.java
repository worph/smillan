package com.smilan.logic.domain.user.function;

import com.google.common.base.Function;
import com.smilan.api.domain.user.User;
import com.smilan.api.domain.user.builder.UserBuilder;
import com.smilan.logic.domain.user.entity.UserEntity;

public class UserEntityToUserFunction implements Function<UserEntity, User> {

    @Override
    public User apply(UserEntity input) {
        return new UserBuilder()
                .withId(""+input.getId())
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
