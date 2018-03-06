/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.logic.domain.media;

import com.smilan.api.domain.media.Media;
import com.smilan.logic.common.EntityDAO;
import com.smilan.logic.domain.media.entity.MediaEntity;

/**
 *
 * @author pierr
 */
public interface MediaDAOInterface extends EntityDAO<MediaEntity> {
    public MediaEntity retreiveOrCreate(Media categories);
    
}
