package com.smilan.test.domain.announce.support;

import com.smilan.api.domain.announce.GeoLocation;
import com.smilan.api.domain.announce.builder.AnnounceBuilder;
import com.smilan.api.domain.announce.builder.GeoLocationBuilder;
import java.util.Arrays;

/**
 * @author Thomas
 *
 */
public class AnnounceDataTest {

    public AnnounceBuilder builder1() {
        return new AnnounceBuilder()
                .withId(null)
                .withText("a2")
                .withTitle("a3")
                .withType("snapan")
                .withProfileId("3")
                .withLocations(Arrays.asList(new GeoLocationBuilder().withLat(1.0f).withLon(2.0f).build()));
    }

    public AnnounceBuilder builder2() {
        return new AnnounceBuilder()
                .withId(null)
                .withText("b2")
                .withType("longan")
                .withTitle("b3")
                .withProfileId("4")
                .withLocations(Arrays.asList(new GeoLocationBuilder().withLat(3.0f).withLon(4.0f).build()));
    }

}
