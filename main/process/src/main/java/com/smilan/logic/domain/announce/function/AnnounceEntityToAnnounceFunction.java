package com.smilan.logic.domain.announce.function;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.smilan.api.domain.announce.Announce;
import com.smilan.api.domain.announce.builder.AnnounceBuilder;
import com.smilan.api.domain.announce.builder.GeoLocationBuilder;
import com.smilan.api.domain.category.Category;
import com.smilan.api.domain.media.Media;
import com.smilan.logic.domain.announce.entity.AnnounceEntity;
import com.smilan.logic.domain.category.entity.CategoryEntity;
import com.smilan.logic.domain.media.entity.MediaEntity;
import java.util.ArrayList;
import java.util.Arrays;
import net.karneim.pojobuilder.GeneratePojoBuilder;

@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class AnnounceEntityToAnnounceFunction implements Function<AnnounceEntity, Announce> {
    private Function<MediaEntity, Media> media;
    private Function<CategoryEntity, Category> category;

    @Override
    public Announce apply(AnnounceEntity input) {
        return new AnnounceBuilder()
                .withId(""+input.getId())
                .withText(input.getText())
                .withTitle(input.getTitle())
                .withType(input.getType())
                .withProfileId(""+input.getProfileId())
                .withChatId(input.getChatId())
                .withMedia(input.getMedia()==null?null:Lists.newArrayList(Iterables.transform(input.getMedia(),media)))
                .withCategories(input.getCategories()==null?null:Lists.newArrayList(Iterables.transform(input.getCategories(),category)))
                .withLocations(new ArrayList<>(Arrays.asList(new GeoLocationBuilder().copy(input.getLocation()).build())))
                .withCreated(input.getCreated())
                .build();
    }

    public Function<MediaEntity, Media> getMedia() {
        return media;
    }

    public void setMedia(Function<MediaEntity, Media> media) {
        this.media = media;
    }

    public Function<CategoryEntity, Category> getCategory() {
        return category;
    }

    public void setCategory(Function<CategoryEntity, Category> category) {
        this.category = category;
    }

}
