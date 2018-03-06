/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.api.common.manager;

import com.smilan.api.common.manager.option.OptionService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Worph
 */
public class EntityManagerDTO<T> implements Serializable {

    private OptionService optionService;

    private List<T> entities = new ArrayList<>();

    public EntityManagerDTO() {
    }

    public OptionService getOptionService() {
        return optionService;
    }

    public void setOptionService(OptionService optionService) {
        this.optionService = optionService;
    }

    public List<T> getEntities() {
        return entities;
    }

    public void setEntities(List<T> entity) {
        this.entities = entity;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.optionService);
        hash = 89 * hash + Objects.hashCode(this.entities);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EntityManagerDTO<?> other = (EntityManagerDTO<?>) obj;
        if (!Objects.equals(this.optionService, other.optionService)) {
            return false;
        }
        if (!Objects.equals(this.entities, other.entities)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EntitySet{" + "optionService=" + optionService + ", entity=" + entities + '}';
    }

    

}
