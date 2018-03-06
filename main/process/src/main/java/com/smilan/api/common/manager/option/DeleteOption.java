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
public class DeleteOption implements Serializable,Option{
    private boolean delete;
    private int deletedNumber;

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public int getDeletedNumber() {
        return deletedNumber;
    }

    public void setDeletedNumber(int deletedNumber) {
        this.deletedNumber = deletedNumber;
    }
        
}
