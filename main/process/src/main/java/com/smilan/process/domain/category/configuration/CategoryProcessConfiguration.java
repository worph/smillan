package com.smilan.process.domain.category.configuration;

import com.smilan.api.domain.category.CategoryManager;
import com.smilan.logic.domain.category.CategoryLogic;
import com.smilan.logic.domain.category.configuration.CategoryLogicConfiguration;
import com.smilan.process.domain.security.AdministrationExcutor;
import com.smilan.logic.domain.security.Realm;
import com.smilan.logic.domain.security.configuration.RealmConfiguration;
import com.smilan.process.domain.category.builder.DefaultCategoryManagerBuilder;
import com.smilan.process.domain.security.configuration.SecurityConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Thomas
 *
 */
@Configuration
@Import({
    CategoryLogicConfiguration.class,
    RealmConfiguration.class,
    SecurityConfiguration.class
        })
public class CategoryProcessConfiguration {

    @Bean
    public CategoryManager categoryManager(
            CategoryLogic personneLogic,
            AdministrationExcutor administrationExcutor,
            Realm customRealm) {
        return new DefaultCategoryManagerBuilder()
                .withCategoryLogic(personneLogic)
                .withAdministrationExcutor(administrationExcutor)
                .withRealm(customRealm)
                .build();
    }
    
}
