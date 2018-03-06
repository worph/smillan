/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.api.common.manager;

import com.smilan.api.common.manager.option.builder.ErrorOptionBuilder;
import com.smilan.api.common.manager.option.builder.OptionServiceBuilder;

/**
 *
 * @author pierr
 */
public class DTOErrorHelper {
    public static <T extends EntityManagerDTO> T makeError(T entity,String error){
        entity.setEntities(null);
        if(entity.getOptionService()==null){
            entity.setOptionService(new OptionServiceBuilder().build());
        }
        entity.getOptionService().setErrorOption(new ErrorOptionBuilder().withError(error).build());
        return entity;
    }
}
