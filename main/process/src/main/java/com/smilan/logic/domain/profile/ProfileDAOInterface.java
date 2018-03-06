/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.logic.domain.profile;

import com.smilan.logic.common.EntityDAO;
import com.smilan.logic.domain.profile.entity.ProfileEntity;

/**
 *
 * @author pierr
 */
public interface ProfileDAOInterface  extends EntityDAO<ProfileEntity>{
    public Long getIDFromUserID(Long userID);
}
