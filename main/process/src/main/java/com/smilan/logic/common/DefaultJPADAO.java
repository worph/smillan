/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.logic.common;

import com.smilan.logic.domain.security.CrudPermission;
import com.google.common.base.Preconditions;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.smilan.api.common.manager.option.DeleteOption;
import com.smilan.api.common.manager.option.OptionService;
import com.smilan.api.common.manager.option.Order;
import com.smilan.api.common.manager.option.SearchOption;
import com.smilan.logic.domain.security.Realm;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

/**
 *
 * @author Worph
 */
public abstract class DefaultJPADAO<T extends BasicEntity> implements EntityDAO<T> {

    protected EntityManager entityManager;
    private CrudPermission crudPermission;

    /**
     * @param entity
     * @see
     * com.smilan.logic.personne.logic.PersonneDAO#save(com.smilan.logic.personne.logic.entity.Personne)
     */
    @Override
    public T save(T entity) {
        Preconditions.checkNotNull(entity, "entity must not be null");

        if (entity.getId() == null) {
            //check this entity is not managed
            Preconditions.checkArgument(!this.entityManager.contains(entity));
            //Create entity
            crudPermission.checkCreatePermission();
            this.entityManager.persist(entity);
        } else {
            //update of an entity
            T persisted = load(entity);
            entity.copyNonNullValueTo(persisted, crudPermission);
            entity = this.entityManager.merge(persisted);
        }
        this.entityManager.flush();
        return entity;
    }

    /**
     * @param entity
     * @see
     * com.smilan.logic.personne.logic.PersonneDAO#load(com.smilan.logic.personne.logic.entity.Personne)
     */
    @Override
    public T load(T entity) {
        Preconditions.checkNotNull(entity, "entity must not be null");
        Preconditions.checkNotNull(entity.getId(), "entity.identifiant must not be null");

        try {
            final Class<T> aClass = (Class<T>) entity.getClass();
            //TODO check read
            return this.entityManager.getReference(aClass, entity.getId());
        } catch (EntityNotFoundException entityNotFoundException) {
            throw new Error(entityNotFoundException);
        }
    }

    /**
     * @param criteria
     * @param searchOption
     * @param deleteOption
     * @see com.smilan.logic.personne.logic.PersonneDAO#search(java.util.List,
     * com.smilan.api.domaine.commun.api.dto.OptionRecherche)
     */
    @Override
    public List<T> search(List<T> criteria, OptionService optionService) {
        SearchOption searchOption = null;
        DeleteOption deleteOption = null;
        if (optionService != null) {
            searchOption = optionService.getSearchOption();
            deleteOption = optionService.getDeleteOption();
        }
        JPAQueryFactory queryFactory = new JPAQueryFactory(this.entityManager);
        BooleanBuilder whereData = new BooleanBuilder();
        List<Predicate> predicates = new ArrayList<>();
        EntityPathBase<T> qEntity = forgeWhere(predicates, criteria, optionService);

        if (searchOption == null
                ? true
                : (searchOption.getExpression() == null ? true : !searchOption.getExpression().equals(SearchOption.EXP_OR))) {
            for (Predicate predicate : predicates) {
                whereData.and(predicate);
            }
        } else {
            for (Predicate predicate : predicates) {
                whereData.or(predicate);
            }
        }

        if (deleteOption == null ? true : !deleteOption.isDelete()) {
            final JPAQuery<T> query = queryFactory.selectFrom(qEntity).where(whereData);
            if (searchOption != null) {
                if (searchOption.getNumber() != null) {
                    if (searchOption.getPage() != null) {
                        query.offset(searchOption.getPage() * searchOption.getNumber());
                    }
                    query.limit(searchOption.getNumber());
                }
                if (searchOption.getOrder() != null) {
                    final List<OrderSpecifier<?>> orderData = forgeOrder(searchOption.getOrder());
                    query.orderBy(orderData.toArray(new OrderSpecifier<?>[0]));
                    //query.orderBy(Expressions.numberTemplate(cl, template, args))
                }
            }
            //read entity remove unallowed fields
            //TODO check read
            return query.fetch();
        } else {
            //delete entity (make a search on id check permission then delete)            
            final JPADeleteClause delete = createDeleteQueryAndCheckPermission(whereData);
            final long execute = delete.execute();
            deleteOption.setDeletedNumber((int) execute);
            return null;
        }

    }

    public JPADeleteClause createDeleteQueryAndCheckPermission(BooleanBuilder whereData) {
        throw new UnsupportedOperationException();
    }

    public EntityPathBase<T> forgeWhere(List<Predicate> predicates, List<T> criteria, OptionService optionService) {
        throw new UnsupportedOperationException();
    }

    public List<OrderSpecifier<?>> forgeOrder(List<Order> order) {
        throw new UnsupportedOperationException();
    }

    /**
     * @return the entityManager
     */
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    /**
     * @param entityManager the entityManager to set
     */
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public CrudPermission getCrudPermission() {
        return crudPermission;
    }

    public void setCrudPermission(CrudPermission crudPermission) {
        this.crudPermission = crudPermission;
    }

}
