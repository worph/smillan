/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.logic.domain.media;

import com.google.common.base.Function;
import com.smilan.api.domain.media.Media;
import com.smilan.api.domain.media.MediaManagerDTO;
import com.smilan.api.domain.media.builder.MediaManagerDTOBuilder;
import com.smilan.logic.domain.security.CrudPermission;
import com.smilan.logic.common.DefaultEntityLogic;
import com.smilan.logic.common.EntityDAO;
import com.smilan.logic.domain.media.entity.MediaEntity;
import java.util.List;

/**
 *
 * @author Worph
 */
public class DefaultMediaLogic extends DefaultEntityLogic<MediaEntity, Media, MediaManagerDTO> implements MediaLogic {

    public DefaultMediaLogic(EntityDAO<MediaEntity> announceDAO, 
            Function<Media, MediaEntity> mediaToMediaEntityFunction, 
            Function<MediaEntity, Media> mediaEntityToMediaFunction) {
        super(announceDAO, mediaToMediaEntityFunction, mediaEntityToMediaFunction);
    }

    @Override
    public MediaManagerDTO buildEntityManagerDTO(List<Media> announces) {
        return new MediaManagerDTOBuilder().withEntities(announces).build();
    }

    @Override
    public void securityFilterData(Media entity, CrudPermission crudPermission) {
        //always authorized
    }

}
