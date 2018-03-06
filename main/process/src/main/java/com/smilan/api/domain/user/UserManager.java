/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.api.domain.user;

import com.smilan.api.common.manager.EntityManager;

/**
 *
 * @author Worph
 */
public interface UserManager extends EntityManager<UserManagerDTO> {

    public UserManagerDTO authSearch(UserAuthToken authToken);
    public void changeLoginPassword(String newLogin,String newPassword,String oldLogin, String oldPassword);
    public void changePassword(String login, String newPassword);
    public User getUserFromUsername(String username);
    public User getUserFromApiKey(String apiKey);
}
