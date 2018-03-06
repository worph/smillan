package com.smilan.api.domain.user;

import com.smilan.api.common.support.CommunHelper;
import com.smilan.api.domain.user.builder.UserBuilder;
import com.smilan.api.domain.user.builder.UserManagerDTOBuilder;
import java.util.ArrayList;
import java.util.List;


public class UserCopyHelper {

    private final CommunHelper communHelper = new CommunHelper();

    public UserManagerDTO copy(UserManagerDTO gererUserDTO) {
        if (gererUserDTO == null) {
            return null;
        }
        return new UserManagerDTOBuilder().copy(gererUserDTO)
                .withOptionService(this.communHelper.copy(gererUserDTO.getOptionService()))
                .withEntities(this.copy(gererUserDTO.getEntities()))
                .build();
    }

    public List<User> copy(List<User> personnes) {
        if (personnes == null) {
            return null;
        }
        List<User> clonedUsers = new ArrayList<>();
        for (User personne : personnes) {
            clonedUsers.add(this.copy(personne));
        }
        return clonedUsers;
    }

    public User copy(User personne) {
        if (personne == null) {
            return null;
        }
        return new UserBuilder().copy(personne).build();
    }

}
