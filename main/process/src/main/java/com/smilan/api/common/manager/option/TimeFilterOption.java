/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.api.common.manager.option;

import java.io.Serializable;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 *
 * @author pierr
 */
@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class TimeFilterOption implements Serializable,Option{
    private int lastUpdatedMin;
    private int lastCreatedMin;

    public int getLastUpdatedMin() {
        return lastUpdatedMin;
    }

    public void setLastUpdatedMin(int lastUpdatedMin) {
        this.lastUpdatedMin = lastUpdatedMin;
    }

    public int getLastCreatedMin() {
        return lastCreatedMin;
    }

    public void setLastCreatedMin(int lastCreatedMin) {
        this.lastCreatedMin = lastCreatedMin;
    }

    
    
}
