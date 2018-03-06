package com.smilan.api.domain.media;

import com.smilan.api.common.support.CommunHelper;
import com.smilan.api.domain.media.builder.MediaBuilder;
import com.smilan.api.domain.media.builder.MediaManagerDTOBuilder;
import java.util.ArrayList;
import java.util.List;


public class MediaCopyHelper {

    private final CommunHelper communHelper = new CommunHelper();

    public MediaManagerDTO copy(MediaManagerDTO gererMediaDTO) {
        if (gererMediaDTO == null) {
            return null;
        }
        return new MediaManagerDTOBuilder().copy(gererMediaDTO)
                .withOptionService(this.communHelper.copy(gererMediaDTO.getOptionService()))
                .withEntities(this.copy(gererMediaDTO.getEntities()))
                .build();
    }

    public List<Media> copy(List<Media> personnes) {
        if (personnes == null) {
            return null;
        }
        List<Media> clonedMedias = new ArrayList<>();
        for (Media personne : personnes) {
            clonedMedias.add(this.copy(personne));
        }
        return clonedMedias;
    }

    public Media copy(Media personne) {
        if (personne == null) {
            return null;
        }
        return new MediaBuilder().copy(personne).build();
    }

}
