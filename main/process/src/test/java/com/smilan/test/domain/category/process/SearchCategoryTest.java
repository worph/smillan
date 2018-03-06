package com.smilan.test.domain.category.process;

import com.smilan.api.common.dto.Contexte;
import com.smilan.api.domain.category.Category;
import com.smilan.api.domain.category.CategoryManager;
import com.smilan.api.domain.category.CategoryManagerDTO;
import com.smilan.api.domain.category.builder.CategoryBuilder;
import com.smilan.api.domain.category.builder.CategoryManagerDTOBuilder;
import com.smilan.logic.common.configuration.CommunLogicConfiguration;
import com.smilan.process.domain.category.configuration.CategoryProcessConfiguration;
import com.smilan.test.common.data.CommunDataTest;
import com.smilan.test.common.support.configuration.CommonConfiguration;
import com.smilan.test.domain.category.support.CategoryDataTest;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Thomas
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CommonConfiguration.class, CategoryProcessConfiguration.class})
@ActiveProfiles(profiles = {"EmbeddedDatabase", "Hibernate", "EmbeddedH2", "HikariCP"})
@TestPropertySource(properties = {"javax.persistence.schema-generation.database.action=drop-and-create"})
public class SearchCategoryTest {

    @Autowired
    private CategoryManager gererCategory;

    public CategoryManagerDTO defineCategory(CategoryManagerDTO gererCategoryDTO) {
        int numberOfCategory = gererCategoryDTO.getEntities().size();
        gererCategoryDTO = this.gererCategory.define(gererCategoryDTO);

        Assert.assertNotNull(gererCategoryDTO);

        List<Category> personnes = gererCategoryDTO.getEntities();
        Assert.assertNotNull(personnes);
        Assert.assertEquals(numberOfCategory, personnes.size());
        Assert.assertNotNull(personnes.get(0));

        return gererCategoryDTO;
    }

    @Test
    public void searchCategoryByValue() {
        CategoryDataTest personneDataTest = new CategoryDataTest();

        CategoryManagerDTO gererCategoryDTO = this.defineCategory(new CategoryManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        personneDataTest.builder1().withValue("ztitle").build()))
                .build());

        Category personneExpected = gererCategoryDTO.getEntities().get(0);

        gererCategoryDTO = SearchCategoryTest.this.gererCategory.search(new CategoryManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        new CategoryBuilder().withValue("ztitle").build()))
                .build());

        Assert.assertNotNull(gererCategoryDTO);

        List<Category> personnes = gererCategoryDTO.getEntities();
        Assert.assertNotNull(personnes);
        Assert.assertEquals(1, personnes.size());

        Category personne = personnes.get(0);
        Assert.assertEquals(personneExpected, personne);
    }
}
