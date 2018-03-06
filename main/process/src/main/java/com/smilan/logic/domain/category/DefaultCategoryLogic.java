package com.smilan.logic.domain.category;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.smilan.api.domain.category.Category;
import com.smilan.api.domain.category.CategoryManagerDTO;
import com.smilan.api.domain.category.builder.CategoryManagerDTOBuilder;
import com.smilan.logic.domain.security.CrudPermission;
import com.smilan.logic.common.DefaultEntityLogic;
import com.smilan.logic.domain.category.entity.CategoryEntity;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Worph
 */
public class DefaultCategoryLogic extends DefaultEntityLogic<CategoryEntity, Category, CategoryManagerDTO> implements CategoryLogic {

    public DefaultCategoryLogic(
            CategoryDAOI categoryDAO,
            Function<Category, CategoryEntity> categoryToCategoryEntityFunction, 
            Function<CategoryEntity, Category> categoryEntityToCategoryFunction) {
        super(categoryDAO, categoryToCategoryEntityFunction, categoryEntityToCategoryFunction);
    }

    @Override
    public CategoryManagerDTO buildEntityManagerDTO(List<Category> categorys) {
        return new CategoryManagerDTOBuilder().withEntities(categorys).build();
    }

    @Override
    public void securityFilterData(Category entity, CrudPermission crudPermission) {
        entity.setPasswordValue(null);//obfuscate password value
    }

    @Override
    public Category searchCategoryByName(String category) {
        CategoryEntity ret = ((CategoryDAOI)entityDAO).loadByName(category);
        if(ret==null){
            return null;
        }
        //TODO SERCURITY filter password value
        return EntityToApiFunction.apply(ret);
    }

    @Override
    public List<Category> list() {
        List<CategoryEntity> ret = ((CategoryDAOI)entityDAO).list();
        final List<Category> transform = new ArrayList<>(Lists.transform(ret, EntityToApiFunction));
        for (Category category : transform) {
            securityFilterData(category, null);
        }
        return transform;
    }

}
