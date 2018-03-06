package com.smilan.api.domain.profile;

import com.smilan.api.common.support.CommunHelper;
import com.smilan.api.domain.profile.builder.ProfileBuilder;
import com.smilan.api.domain.profile.builder.ProfileManagerDTOBuilder;
import java.util.ArrayList;
import java.util.List;


public class ProfileCopyHelper {

    private final CommunHelper communHelper = new CommunHelper();

    public ProfileManagerDTO copy(ProfileManagerDTO gererUserDTO) {
        if (gererUserDTO == null) {
            return null;
        }
        return new ProfileManagerDTOBuilder().copy(gererUserDTO)
                .withOptionService(this.communHelper.copy(gererUserDTO.getOptionService()))
                .withEntities(this.copy(gererUserDTO.getEntities()))
                .build();
    }

    public List<Profile> copy(List<Profile> personnes) {
        if (personnes == null) {
            return null;
        }
        List<Profile> clonedUsers = new ArrayList<>();
        for (Profile personne : personnes) {
            clonedUsers.add(this.copy(personne));
        }
        return clonedUsers;
    }

    public Profile copy(Profile personne) {
        if (personne == null) {
            return null;
        }
        return new ProfileBuilder().copy(personne).build();
    }

}
