package com.smilan.logic.domain.media.function;

import com.google.common.base.Function;
import com.smilan.api.domain.media.Media;
import com.smilan.api.domain.media.builder.MediaBuilder;
import com.smilan.logic.domain.media.entity.MediaEntity;
import net.karneim.pojobuilder.GeneratePojoBuilder;

@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class MediaEntityToMediaFunction implements Function<MediaEntity, Media> {

    @Override
    public Media apply(MediaEntity input) {
        if(input==null) return null;
        return new MediaBuilder()
                .withId(""+input.getId())
                .withType(input.getType())
                .withUrl(input.getUrl())
                .build();
    }

}
