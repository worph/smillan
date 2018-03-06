package com.smilan.logic.domain.profile.function;

import com.google.common.base.Function;
import com.smilan.api.domain.media.Media;
import com.smilan.api.domain.profile.Profile;
import com.smilan.api.domain.profile.builder.ProfileBuilder;
import com.smilan.logic.domain.media.entity.MediaEntity;
import com.smilan.logic.domain.profile.entity.ProfileEntity;
import net.karneim.pojobuilder.GeneratePojoBuilder;

@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class ProfileEntityToProfileFunction implements Function<ProfileEntity, Profile> {
    private Function<MediaEntity, Media> media;

    @Override
    public Profile apply(ProfileEntity input) {
        return new ProfileBuilder()
                .withId(""+input.getId())
                .withUserId(""+input.getUserId())
                .withProfileName(input.getProfileName())
                .withXmppId(input.getXmppId())
                .withDescription(input.getDescription())
                .withAvatar(media.apply(input.getAvatar()))
                .withBackground(media.apply(input.getBackground()))
                .build();
    }

    public Function<MediaEntity, Media> getMedia() {
        return media;
    }

    public void setMedia(Function<MediaEntity, Media> media) {
        this.media = media;
    }
    

}
