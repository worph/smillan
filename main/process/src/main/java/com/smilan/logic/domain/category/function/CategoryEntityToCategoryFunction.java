package com.smilan.logic.domain.category.function;

import com.google.common.base.Function;
import com.smilan.api.domain.category.Category;
import com.smilan.api.domain.category.builder.CategoryBuilder;
import com.smilan.logic.domain.category.entity.CategoryEntity;

public class CategoryEntityToCategoryFunction implements Function<CategoryEntity, Category> {
    
    @Override
    public Category apply(CategoryEntity input) {
        return new CategoryBuilder()
                .withValue(input.getValue())
                .withPasswordValue(input.getPassword())
                .withPassword(input.getPassword()!=null)//password can be null
                .build();
    }
    
}
