package com.smilan.logic.domain.category;

import com.smilan.api.domain.category.Category;
import com.smilan.api.domain.category.CategoryManagerDTO;
import com.smilan.logic.common.EntityLogic;
import java.util.List;

/**
 *
 * @author Worph
 */
public interface CategoryLogic extends EntityLogic<CategoryManagerDTO> {

    public Category searchCategoryByName(String category);

    public List<Category> list();

}
