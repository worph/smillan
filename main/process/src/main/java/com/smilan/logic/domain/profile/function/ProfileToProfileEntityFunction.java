package com.smilan.logic.domain.profile.function;

import com.google.common.base.Function;
import com.smilan.api.domain.media.Media;
import com.smilan.api.domain.profile.Profile;
import com.smilan.logic.domain.media.entity.MediaEntity;
import com.smilan.logic.domain.profile.entity.ProfileEntity;
import com.smilan.logic.domain.profile.entity.builder.ProfileEntityBuilder;
import net.karneim.pojobuilder.GeneratePojoBuilder;

@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class ProfileToProfileEntityFunction implements Function<Profile, ProfileEntity> {
    private Function<Media, MediaEntity> media;

    @Override
    public ProfileEntity apply(Profile input) {
        return new ProfileEntityBuilder()
                .withId(input.getId()==null?null:Long.parseLong(input.getId()))
                .withUserId(input.getUserId()==null?null:Long.parseLong(input.getUserId()))
                .withProfileName(input.getProfileName())
                .withXmppId(input.getXmppId())
                .withDescription(input.getDescription())
                .withAvatar(media.apply(input.getAvatar()))
                .withBackground(media.apply(input.getBackground()))
                .build();
    }

    public Function<Media, MediaEntity> getMedia() {
        return media;
    }

    public void setMedia(Function<Media, MediaEntity> media) {
        this.media = media;
    }
}
