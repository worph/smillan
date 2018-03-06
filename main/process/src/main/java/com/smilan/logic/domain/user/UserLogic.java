/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.logic.domain.user;

import com.smilan.api.domain.user.User;
import com.smilan.api.domain.user.UserManagerDTO;
import com.smilan.logic.common.EntityLogic;

/**
 *
 * @author Worph
 */
public interface UserLogic extends EntityLogic<UserManagerDTO> {
    public User getUserFromUsername(String username);
    public User getUserFromApiKey(String apiKey);
    public User getUserFromUserId(String userId);
    public void setLogin(String id,String login);
    public void setPassword(String id, String password);

    public User set(User user);
}
