/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.logic.domain.profile;

import com.google.common.base.Function;
import com.smilan.api.domain.profile.Profile;
import com.smilan.api.domain.profile.ProfileManagerDTO;
import com.smilan.api.domain.profile.builder.ProfileManagerDTOBuilder;
import com.smilan.logic.domain.security.CrudPermission;
import com.smilan.logic.common.DefaultEntityLogic;
import com.smilan.logic.common.EntityDAO;
import com.smilan.logic.domain.profile.entity.ProfileEntity;
import com.smilan.logic.domain.security.Realm;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Worph
 */
public class DefaultProfileLogic extends DefaultEntityLogic<ProfileEntity, Profile, ProfileManagerDTO> implements ProfileLogic {
    private final ProfileDAOInterface profileDAOInterface;
    
    public DefaultProfileLogic(
            ProfileDAOInterface profileDAO, 
            Function<Profile, ProfileEntity> profileToAnnounceEntityFunction, 
            Function<ProfileEntity, Profile> profileEntityToAnnounceFunction) {
        super(profileDAO, profileToAnnounceEntityFunction, profileEntityToAnnounceFunction);
        profileDAOInterface = profileDAO;
    }

    @Override
    public ProfileManagerDTO buildEntityManagerDTO(List<Profile> profiles) {
        return new ProfileManagerDTOBuilder().withEntities(profiles).build();
    }

    @Override
    public Long getIDFromUserID(Long userID) {
        return profileDAOInterface.getIDFromUserID(userID);
    }

    @Override
    public void securityFilterData(Profile entity, CrudPermission crudPermission) {
        long id = Long.parseLong(entity.getId());
        if(!crudPermission.hasReadPermission(id, ProfileEntity.NAME_AVATAR)){
            entity.setAvatar(null);
        }
        if(!crudPermission.hasReadPermission(id, ProfileEntity.NAME_BACKGROUND)){
            entity.setBackground(null);
        }
        if(!crudPermission.hasReadPermission(id, ProfileEntity.NAME_DESCRIPTION)){
            entity.setDescription(null);
        }
        if(!crudPermission.hasReadPermission(id, ProfileEntity.NAME_PROFILENAME)){
            entity.setProfileName(null);
        }
        if(!crudPermission.hasReadPermission(id, ProfileEntity.NAME_USERID)){
            entity.setUserId(null);
        }
        if(!crudPermission.hasReadPermission(id, ProfileEntity.NAME_XMPPID)){
            entity.setXmppId(null);
        }
    }

}
