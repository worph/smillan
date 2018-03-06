package com.smilan.test.domain.category.api;

import com.smilan.api.domain.category.CategoryCopyHelper;
import com.smilan.api.domain.category.CategoryManagerDTO;
import com.smilan.api.domain.category.builder.CategoryManagerDTOBuilder;
import com.smilan.test.domain.category.support.CategoryDataTest;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Thomas
 *
 */
public class CategoryCopyHelperTest {

    private final CategoryCopyHelper categoryHelper = new CategoryCopyHelper();

    @Test
    public void copyTest() {
        CategoryDataTest categoryDataTest = new CategoryDataTest();
        CategoryManagerDTO categorySet = new CategoryManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        categoryDataTest.builder1().build(),
                        categoryDataTest.builder2().build()
                )).build();

        Assert.assertEquals(categorySet, this.categoryHelper.copy(categorySet));
    }

}
