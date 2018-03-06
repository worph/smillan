/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.logic.common;

import com.smilan.logic.domain.security.CrudPermission;

/**
 *
 * @author Worph
 */
public interface BasicEntity<T extends BasicEntity> {

    Long getId();

    public void copyNonNullValueTo(T to,CrudPermission crudPermission);

}
