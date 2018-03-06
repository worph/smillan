/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.logic.domain.profile;

import com.smilan.api.domain.profile.ProfileManagerDTO;
import com.smilan.logic.common.EntityLogic;

/**
 *
 * @author Worph
 */
public interface ProfileLogic extends EntityLogic<ProfileManagerDTO> {
    public Long getIDFromUserID(Long userID);
}
