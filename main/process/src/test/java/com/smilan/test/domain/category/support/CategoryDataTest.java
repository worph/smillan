package com.smilan.test.domain.category.support;

import com.smilan.api.domain.category.builder.CategoryBuilder;

/**
 * @author Thomas
 *
 */
public class CategoryDataTest {

    public CategoryBuilder builder1() {
        return new CategoryBuilder()
                .withValue("value");
    }

    public CategoryBuilder builder2() {
        return new CategoryBuilder()
                .withValue(new StringBuilder("value").reverse().toString());
    }

}
