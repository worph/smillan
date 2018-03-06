package com.smilan.test.domain.user.support;

import com.smilan.api.domain.user.builder.UserBuilder;

/**
 * @author Thomas
 *
 */
public class UserDataTest {

    public UserBuilder builder1() {
        return new UserBuilder()
                .withId(null)
                .withLogin("uniquelogin1")
                .withPassword("password")
                .withXmppPassword("xmpppasword")
                .withEmail("email")
                .withApiKey("test");
    }

    public UserBuilder builder2() {
        return new UserBuilder()
                .withId(null)
                .withLogin(new StringBuilder("uniquelogin2").reverse().toString())
                .withPassword(new StringBuilder("password").reverse().toString())
                .withXmppPassword(new StringBuilder("xmpppasword").reverse().toString())
                .withEmail(new StringBuilder("email").reverse().toString())
                .withApiKey(new StringBuilder("apikey").reverse().toString());
    }

}
