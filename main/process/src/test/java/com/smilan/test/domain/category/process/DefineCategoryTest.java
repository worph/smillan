package com.smilan.test.domain.category.process;

import com.smilan.api.common.dto.Contexte;
import com.smilan.api.domain.category.Category;
import com.smilan.api.domain.category.CategoryManager;
import com.smilan.api.domain.category.CategoryManagerDTO;
import com.smilan.api.domain.category.builder.CategoryManagerDTOBuilder;
import com.smilan.logic.common.configuration.CommunLogicConfiguration;
import com.smilan.process.domain.announce.configuration.AnnounceProcessConfiguration;
import com.smilan.process.domain.category.configuration.CategoryProcessConfiguration;
import com.smilan.test.common.data.CommunDataTest;
import com.smilan.test.common.support.configuration.CommonConfiguration;
import com.smilan.test.domain.category.support.CategoryDataTest;
import java.util.Arrays;
import java.util.Iterator;
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
 * TODO TEST on lock, intigrity
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CommonConfiguration.class, CategoryProcessConfiguration.class})
@ActiveProfiles(profiles = {"EmbeddedDatabase", "Hibernate", "EmbeddedH2", "HikariCP"})
@TestPropertySource(properties = {"javax.persistence.schema-generation.database.action=drop-and-create"})
public class DefineCategoryTest {

    @Autowired
    private CategoryManager categoryManager;

    @Test
    public void defineCategoryTest() {
        CategoryDataTest categoryDataTest = new CategoryDataTest();

        CategoryManagerDTO categoryManagerDTO = new CategoryManagerDTOBuilder()
                .withEntities(Arrays.asList(categoryDataTest.builder1().build()))
                .build();

        Category expectedCategory = categoryManagerDTO.getEntities().get(0);

        categoryManagerDTO = this.categoryManager.define(categoryManagerDTO);

        Assert.assertNotNull(categoryManagerDTO);

        List<Category> categorys = categoryManagerDTO.getEntities();
        Assert.assertNotNull(categorys);
        Assert.assertEquals(1, categorys.size());

        Category category = categorys.get(0);
        Assert.assertEquals(expectedCategory, category);
    }

    @Test
    public void defineCategoryExistanteTest() {
        CategoryDataTest categoryDataTest = new CategoryDataTest();

        CategoryManagerDTO categoryManagerDTO = this.categoryManager.define(new CategoryManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        categoryDataTest.builder1().build()))
                .build());

        categoryManagerDTO = new CategoryManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        categoryDataTest.builder2().build()))
                .build();

        Category expectedCategory = categoryManagerDTO.getEntities().get(0);

        categoryManagerDTO = this.categoryManager.define(categoryManagerDTO);

        Assert.assertNotNull(categoryManagerDTO);

        List<Category> categorys = categoryManagerDTO.getEntities();
        Assert.assertNotNull(categorys);
        Assert.assertEquals(1, categorys.size());

        Category category = categorys.get(0);
        Assert.assertEquals(expectedCategory, category);
    }

    @Test
    public void multipleDefinirCategoryTest() {
        CategoryDataTest categoryDataTest = new CategoryDataTest();

        CategoryManagerDTO categoryManagerDTO = this.categoryManager.define(new CategoryManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        categoryDataTest.builder1().build(),
                        categoryDataTest.builder1().build()))
                .build());

        categoryManagerDTO = new CategoryManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        categoryDataTest.builder1().build(),
                        categoryDataTest.builder1().build(),
                        categoryDataTest.builder1().build(),
                        categoryDataTest.builder2().build(),
                        categoryDataTest.builder2().build()))
                .build();

        Iterator<Category> categorysIterator = categoryManagerDTO.getEntities().iterator();
        Category expectedCategory = categorysIterator.next();
        Category expectedCategory2 = categorysIterator.next();
        Category expectedCategory3 = categorysIterator.next();
        Category expectedCategory4 = categorysIterator.next();
        Category expectedCategory5 = categorysIterator.next();

        categoryManagerDTO = this.categoryManager.define(categoryManagerDTO);

        Assert.assertNotNull(categoryManagerDTO);

        List<Category> categorys = categoryManagerDTO.getEntities();
        Assert.assertNotNull(categorys);
        Assert.assertEquals(5, categorys.size());

        categorysIterator = categorys.iterator();
        Category category = categorysIterator.next();
        Assert.assertEquals(expectedCategory, category);
        Category category2 = categorysIterator.next();
        Assert.assertEquals(expectedCategory2, category2);
        Category category3 = categorysIterator.next();
        Assert.assertEquals(expectedCategory3, category3);
        Category category4 = categorysIterator.next();
        Assert.assertEquals(expectedCategory4, category4);
        Category category5 = categorysIterator.next();
        Assert.assertEquals(expectedCategory5, category5);
    }
}
