package com.smilan.logic.domain.category.configuration;

import com.google.common.base.Function;
import com.smilan.api.domain.category.Category;
import com.smilan.logic.domain.security.CrudPermission;
import com.smilan.logic.common.EntityDAO;
import com.smilan.logic.common.configuration.EntityManagerConfiguration;
import com.smilan.logic.domain.category.DefaultCategoryLogic;
import com.smilan.logic.domain.category.CategoryLogic;
import com.smilan.logic.domain.category.builder.JPACategoryDAOBuilder;
import com.smilan.logic.domain.category.entity.CategoryEntity;
import com.smilan.logic.domain.category.function.CategoryEntityToCategoryFunction;
import com.smilan.logic.domain.category.function.CategoryToCategoryEntityFunction;
import com.smilan.logic.domain.category.function.CategoryToCategoryEntityFunctionBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.smilan.logic.domain.category.CategoryDAOI;

/**
 * @author Thomas
 *
 */
@Configuration
public class CategoryLogicConfiguration {

    @Bean
    public CategoryDAOI categoryDAO(EntityManagerConfiguration entityManagerConfiguration) {
        return new JPACategoryDAOBuilder()
                .withEntityManager(entityManagerConfiguration.getEntityManager())
                .withCrudPermission(new CrudPermission("category"))
                .build();
    }

    @Bean
    public CategoryLogic categoryLogic(CategoryDAOI categoryDAO, Function<Category, CategoryEntity> categoryToCategoryEntityFunction,
            Function<CategoryEntity, Category> categoryEntityToCategoryFunction) {
        return new DefaultCategoryLogic(categoryDAO, categoryToCategoryEntityFunction, categoryEntityToCategoryFunction);
    }

    @Bean
    public Function<Category, CategoryEntity> categoryToCategoryEntityFunction(CategoryDAOI categoryDAO) {
        CategoryToCategoryEntityFunction categoryToCategoryLogicFunction = new CategoryToCategoryEntityFunctionBuilder().withCategoryDAO(categoryDAO).build();
        return categoryToCategoryLogicFunction;
    }

    @Bean
    public Function<CategoryEntity, Category> categoryEntityToCategoryFunction() {
        CategoryEntityToCategoryFunction categoryLogicToCategoryFunction = new CategoryEntityToCategoryFunction();
        return categoryLogicToCategoryFunction;
    }

}
