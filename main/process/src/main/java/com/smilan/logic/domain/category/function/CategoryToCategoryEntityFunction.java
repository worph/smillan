package com.smilan.logic.domain.category.function;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.smilan.api.domain.category.Category;
import com.smilan.logic.domain.category.entity.CategoryEntity;
import net.karneim.pojobuilder.GeneratePojoBuilder;
import com.smilan.logic.domain.category.CategoryDAOI;

@GeneratePojoBuilder(withCopyMethod = true)
public class CategoryToCategoryEntityFunction implements Function<Category, CategoryEntity> {

    protected CategoryToCategoryEntityFunction() {
    }
        
    private CategoryDAOI categoryDAO;

    @Override
    public CategoryEntity apply(Category input) {
        Preconditions.checkNotNull(input);
        Preconditions.checkNotNull(input.getValue());
        return categoryDAO.retreiveOrCreate(input.getValue(),input.getPasswordValue());
    }

    public CategoryDAOI getCategoryDAO() {
        return categoryDAO;
    }

    public void setCategoryDAO(CategoryDAOI categoryDAO) {
        this.categoryDAO = categoryDAO;
    }
    
    
}
