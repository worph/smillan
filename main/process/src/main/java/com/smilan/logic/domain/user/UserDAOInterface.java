/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.logic.domain.user;

import com.smilan.logic.common.EntityDAO;
import com.smilan.logic.domain.user.entity.UserEntity;

/**
 *
 * @author pierr
 */
public interface UserDAOInterface extends EntityDAO<UserEntity>{
    public UserEntity getUserFromUsername(String username);
    public UserEntity getUserFromApiKey(String apiKey);
    public UserEntity setUser(UserEntity user);
    public void setLogin(Long id,String login);
    public void setPassword(Long id, String password);

    public UserEntity getUserFromUserId(String userId);
}
