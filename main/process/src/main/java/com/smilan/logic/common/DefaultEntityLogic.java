/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.logic.common;

import com.smilan.logic.domain.security.CrudPermission;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.smilan.api.common.manager.EntityManagerDTO;
import com.smilan.api.common.manager.option.DeleteOption;
import com.smilan.api.common.manager.option.SearchOption;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Worph
 */
public abstract class DefaultEntityLogic<T extends BasicEntity, U, V extends EntityManagerDTO<U>> implements EntityLogic<V> {

    protected final EntityDAO<T> entityDAO;

    protected final Function<U, T> apiToEntityFunction;

    protected final Function<T, U> EntityToApiFunction;

    public DefaultEntityLogic(EntityDAO<T> entityDAO, Function<U, T> apiToEntityFunction, Function<T, U> EntityToApiFunction) {
        this.entityDAO = entityDAO;
        this.apiToEntityFunction = apiToEntityFunction;
        this.EntityToApiFunction = EntityToApiFunction;
    }

    @Override
    public V define(V gererPersonneDTO) {
        List<T> entities = new ArrayList<>();
        for (T entity : Lists.transform(gererPersonneDTO.getEntities(), apiToEntityFunction)) {
            entities.add(this.entityDAO.save(entity));
        }
        return buildEntityManagerDTO(new ArrayList<>(Lists.transform(entities, this.EntityToApiFunction)));
    }

    @Override
    public V search(V gererPersonneDTO) {
        List<T> announces = Lists.transform(gererPersonneDTO.getEntities(), apiToEntityFunction);
        List<T> announcesCriteria = new ArrayList<>();
        List<T> searchResult = new ArrayList<>();
        for (T announce : announces) {
            announcesCriteria.add(announce);
        }
        if (!announcesCriteria.isEmpty()) {
            final List<T> search = this.entityDAO.search(announcesCriteria, gererPersonneDTO.getOptionService());
            if (search != null) {
                //note: null is in case of delete
                searchResult.addAll(search);
            }
        }
        try {
            final List<U> transform = Lists.transform(searchResult, this.EntityToApiFunction);
            for (U u : transform) {
                securityFilterData(u, entityDAO.getCrudPermission());
            }
            final V buildEntityManagerDTO = buildEntityManagerDTO(new ArrayList<>(transform));
            buildEntityManagerDTO.setOptionService(gererPersonneDTO.getOptionService());//for output information ontained in this class
            return buildEntityManagerDTO;
        } catch (javax.persistence.EntityNotFoundException ex) {
            return buildEntityManagerDTO(new ArrayList<>());
        }
    }

    public abstract V buildEntityManagerDTO(List<U> announces);

    public abstract void securityFilterData(U entity, CrudPermission crudPermission);

}
