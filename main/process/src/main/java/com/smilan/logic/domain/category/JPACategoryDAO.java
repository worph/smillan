/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.logic.domain.category;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.smilan.api.common.manager.option.OptionService;
import com.smilan.logic.common.DefaultJPADAO;
import com.smilan.logic.domain.category.entity.QCategoryEntity;
import com.smilan.logic.domain.category.entity.CategoryEntity;
import com.smilan.logic.domain.category.entity.CategoryEntityBuilder;
import java.util.List;
import net.karneim.pojobuilder.GeneratePojoBuilder;

/**
 *
 * @author Worph
 */
@GeneratePojoBuilder(withCopyMethod = true, intoPackage = "*.builder")
public class JPACategoryDAO extends DefaultJPADAO<CategoryEntity> implements CategoryDAOI { 

    @Override
    public EntityPathBase<CategoryEntity> forgeWhere(List<Predicate> predicates, List<CategoryEntity> criteria, OptionService optionService) {
        QCategoryEntity qCategoryEntity = QCategoryEntity.categoryEntity;

        for (CategoryEntity categoryEntity : criteria) {
            if (categoryEntity.getId() != null) {
                predicates.add(qCategoryEntity.id.eq(categoryEntity.getId()));
            }
            if (categoryEntity.getValue() != null) {
                predicates.add(qCategoryEntity.value.eq(categoryEntity.getValue()));
            }
            if (categoryEntity.getPassword() != null) {
                throw new IllegalArgumentException("can't search on category password");
            }
        }
        return qCategoryEntity;
    }

    @Override
    public CategoryEntity retreiveOrCreate(String category, String password) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(this.entityManager);
        final JPAQuery<CategoryEntity> query = queryFactory.select(QCategoryEntity.categoryEntity).from(QCategoryEntity.categoryEntity).where(QCategoryEntity.categoryEntity.value.eq(category));
        final List<CategoryEntity> fetch = query.fetch();
        if (fetch.isEmpty()) {
            return new CategoryEntityBuilder().withValue(category).withPassword(password).build();
        }
        if (fetch.size() == 1) {
            return fetch.get(0);
        }
        throw new Error("Can't happens since category value is a unique field");
    }

    @Override
    public CategoryEntity loadByName(String category) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(this.entityManager);
        final JPAQuery<CategoryEntity> query = queryFactory.select(QCategoryEntity.categoryEntity).from(QCategoryEntity.categoryEntity).where(QCategoryEntity.categoryEntity.value.eq(category));
        final List<CategoryEntity> fetch = query.fetch();
        if (fetch.isEmpty()) {
            return null;
        }
        if (fetch.size() == 1) {
            return fetch.get(0);
        }
        throw new Error("Can't happens since category value is a unique field");
    }

    @Override
    public List<CategoryEntity> list() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(this.entityManager);
        final JPAQuery<CategoryEntity> query = queryFactory.select(QCategoryEntity.categoryEntity).from(QCategoryEntity.categoryEntity);
        final List<CategoryEntity> fetch = query.fetch();
        return fetch;
    }

}
