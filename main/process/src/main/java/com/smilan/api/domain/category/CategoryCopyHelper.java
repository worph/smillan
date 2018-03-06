package com.smilan.api.domain.category;

import com.smilan.api.common.support.CommunHelper;
import com.smilan.api.domain.category.builder.CategoryBuilder;
import com.smilan.api.domain.category.builder.CategoryManagerDTOBuilder;
import java.util.ArrayList;
import java.util.List;


public class CategoryCopyHelper {

    private final CommunHelper communHelper = new CommunHelper();

    public CategoryManagerDTO copy(CategoryManagerDTO gererCategoryDTO) {
        if (gererCategoryDTO == null) {
            return null;
        }
        return new CategoryManagerDTOBuilder().copy(gererCategoryDTO)
                .withOptionService(this.communHelper.copy(gererCategoryDTO.getOptionService()))
                .withEntities(this.copy(gererCategoryDTO.getEntities()))
                .build();
    }

    public List<Category> copy(List<Category> personnes) {
        if (personnes == null) {
            return null;
        }
        List<Category> clonedCategorys = new ArrayList<>();
        for (Category personne : personnes) {
            clonedCategorys.add(this.copy(personne));
        }
        return clonedCategorys;
    }

    public Category copy(Category personne) {
        if (personne == null) {
            return null;
        }
        return new CategoryBuilder().copy(personne).build();
    }

}
