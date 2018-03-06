/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.api.common.manager.option;

import java.io.Serializable;
import java.util.Objects;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 *
 * @author pierr
 */
@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class Order implements Serializable,Option{
    
    public static final String SORT_ASC = "asc";
    public static final String SORT_DESC = "desc";
    
    private String item;
    private String direction;//asc or desc
    private String parameters;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.item);
        hash = 89 * hash + Objects.hashCode(this.direction);
        hash = 89 * hash + Objects.hashCode(this.parameters);
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
        final Order other = (Order) obj;
        if (!Objects.equals(this.item, other.item)) {
            return false;
        }
        if (!Objects.equals(this.direction, other.direction)) {
            return false;
        }
        if (!Objects.equals(this.parameters, other.parameters)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Order{" + "item=" + item + ", direction=" + direction + ", parameters=" + parameters + '}';
    }
    
    
}
