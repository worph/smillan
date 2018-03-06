package com.smilan.logic.domain.announce.function;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.smilan.api.domain.announce.Announce;
import com.smilan.api.domain.announce.GeoLocation;
import com.smilan.api.domain.announce.builder.GeoLocationBuilder;
import com.smilan.api.domain.category.Category;
import com.smilan.api.domain.media.Media;
import com.smilan.logic.domain.announce.entity.AnnounceEntity;
import com.smilan.logic.domain.announce.entity.builder.AnnounceEntityBuilder;
import com.smilan.logic.domain.category.entity.CategoryEntity;
import com.smilan.logic.domain.media.entity.MediaEntity;
import java.util.ArrayList;
import net.karneim.pojobuilder.GeneratePojoBuilder;

@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class AnnounceToAnnounceEntityFunction implements Function<Announce, AnnounceEntity> {
    private Function<Media, MediaEntity> media;
    private Function<Category, CategoryEntity> category;

    @Override
    public AnnounceEntity apply(Announce input) {
        GeoLocation location = null;
        if (input.getLocations() == null) {
                location = new GeoLocationBuilder().build();
        } else {
            if(input.getLocations().isEmpty()){
                location = new GeoLocationBuilder().build();
            }else{
                location = new GeoLocationBuilder().copy(input.getLocations().get(0)).build();
            }
        }
        return new AnnounceEntityBuilder()
                .withId(input.getId()==null?null:Long.parseLong(input.getId()))
                .withText(input.getText())
                .withTitle(input.getTitle())
                .withType(input.getType())
                .withChatId(input.getChatId())
                .withProfileId(input.getProfileId()==null?null:Long.parseLong(input.getProfileId()))
                .withMedia(input.getMedia()==null?new ArrayList<>():
                        Lists.newArrayList(Iterables.transform(input.getMedia(),media)))
                .withCategories(input.getCategories()==null?new ArrayList<>():
                        Lists.newArrayList(Iterables.transform(input.getCategories(),category)))
                .withLocation(location)
                .build();
    }

    public Function<Media, MediaEntity> getMedia() {
        return media;
    }

    public void setMedia(Function<Media, MediaEntity> media) {
        this.media = media;
    }

    public Function<Category, CategoryEntity> getCategory() {
        return category;
    }

    public void setCategory(Function<Category, CategoryEntity> category) {
        this.category = category;
    }

    
}
