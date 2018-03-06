/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.process.domain.security;

import com.smilan.api.domain.user.User;
import com.smilan.logic.domain.user.UserLogic;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;

/**
 *
 * @author pierr
 */
public class RESTApiAuthenticator {
    
    private final UserLogic userLogic;

    public RESTApiAuthenticator(UserLogic userLogic) {
        this.userLogic = userLogic;
    }
    
    public void login(String apiKey){
        final Subject subject = SecurityUtils.getSubject(); 
        if(!subject.isAuthenticated()){
            if(apiKey==null){
                subject.login(new UsernamePasswordToken("guest", "guest"));
            }else{
                final User userFromApiKey = userLogic.getUserFromApiKey(apiKey);
                if(userFromApiKey==null){
                    subject.login(new UsernamePasswordToken("guest", "guest"));  
                }else{
                    subject.login(new UsernamePasswordToken(userFromApiKey.getLogin(), userFromApiKey.getPassword()));
                }
            }
        }
        //here special cas where we are already authenticated (admin basic auth session)
    }
    
}
