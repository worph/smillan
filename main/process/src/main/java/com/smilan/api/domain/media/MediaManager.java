/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.api.domain.media;

import com.smilan.api.common.manager.EntityManager;
import java.util.List;

/**
 *
 * @author Worph
 */
public interface MediaManager extends EntityManager<MediaManagerDTO> {
    public List<Media> getDefaultAvatarList();
    public void addDefaultAvatar(String type,String url);    
    public List<Media> getDefaultBackGroundList();
    public void addDefaultBackGround(String type,String url);
}
