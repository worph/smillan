package com.smilan.test.domain.media.support;

import com.smilan.api.domain.media.builder.MediaBuilder;

/**
 * @author Thomas
 *
 */
public class MediaDataTest {

    public MediaBuilder builder1() {
        return new MediaBuilder()
                .withId(null)
                .withType("type")
                .withUrl("url");
    }

    public MediaBuilder builder2() {
        return new MediaBuilder()
                .withId(null)
                .withType(new StringBuilder("type").reverse().toString())
                .withUrl(new StringBuilder("url").reverse().toString());
    }

}
