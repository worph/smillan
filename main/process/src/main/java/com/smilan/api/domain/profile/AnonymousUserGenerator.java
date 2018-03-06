/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.api.domain.profile;

import com.smilan.api.domain.user.UserManagerDTO;
import com.smilan.api.domain.user.UserUpgradePackage;

/**
 *
 * @author pierr
 */
public interface AnonymousUserGenerator {
    public UserManagerDTO createNewUser();
    public ProfileManagerDTO createNewProfile(String userId);
    public UserManagerDTO upgradeProfile(UserUpgradePackage userUpgradePackage);
}
