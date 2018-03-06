package com.smilan.logic.common.configuration;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EntityManagerConfiguration {

    //EntityManager can't be inject by parameter
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * @param entityManager the entityManager to set
     */
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * @return the entityManager
     */
    public EntityManager getEntityManager() {
        return this.entityManager;
    }
}