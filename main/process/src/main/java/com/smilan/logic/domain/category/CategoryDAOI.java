package com.smilan.logic.domain.category;

import com.smilan.logic.common.EntityDAO;
import com.smilan.logic.domain.category.entity.CategoryEntity;
import java.util.List;
import java.util.Set;

/**
 *
 * @author pierr
 */
public interface CategoryDAOI extends EntityDAO<CategoryEntity> {

    public CategoryEntity retreiveOrCreate(String category, String password);

    public CategoryEntity loadByName(String category);

    public List<CategoryEntity> list();
}
