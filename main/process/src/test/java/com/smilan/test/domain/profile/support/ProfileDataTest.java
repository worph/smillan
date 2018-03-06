package com.smilan.test.domain.profile.support;

import com.smilan.api.domain.media.Media;
import com.smilan.api.domain.media.builder.MediaBuilder;
import com.smilan.api.domain.profile.builder.ProfileBuilder;

/**
 * @author Thomas
 *
 */
public class ProfileDataTest {

    public ProfileBuilder builder1() {
        return new ProfileBuilder()
                .withId(null)
                .withProfileName("login")
                .withUserId("1")
                .withAvatar(new MediaBuilder().withType("toto").withUrl("url").build())
                .withBackground(new MediaBuilder().withType("toto").withUrl("url").build())
                .withDescription("description")
                .withXmppId("email");
    }

    public ProfileBuilder builder2() {
        return new ProfileBuilder()
                .withId(null)
                .withProfileName(new StringBuilder("login").reverse().toString())
                .withUserId("2")
                .withAvatar(new MediaBuilder().withType("toto").withUrl("url").build())
                .withBackground(new MediaBuilder().withType("toto").withUrl("url").build())
                .withDescription("description")
                .withXmppId(new StringBuilder("email").reverse().toString());
    }

}
