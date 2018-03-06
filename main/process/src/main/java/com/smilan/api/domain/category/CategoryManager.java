/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.api.domain.category;

import com.smilan.api.common.manager.EntityManager;
import com.smilan.process.domain.category.CategoryAuthResult;
import com.smilan.process.domain.category.CategoryToken;

/**
 *
 * @author Worph
 */
public interface CategoryManager extends EntityManager<CategoryManagerDTO> {
    public CategoryAuthResult auth(CategoryToken token);

    public CategoryManagerDTO list();
}
