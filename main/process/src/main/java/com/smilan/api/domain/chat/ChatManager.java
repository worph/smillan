/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.api.domain.chat;

import java.util.ArrayList;

/**
 *
 * @author pierr
 */
public interface ChatManager {
    /**
     * 
     * @param user not a JID
     * @param password
     * @return jabber id of the created user
     */
    public String createUser(String user,String password);
    
    /**
     * 
     * @param userA a JID
     * @param userB a JID
     */
    public void linkUsers(String userA, String userB);
    
    /**
     * 
     * @param prefix
     * @return 
     */
    public ArrayList<String> cleanUsers(String prefix);
}
