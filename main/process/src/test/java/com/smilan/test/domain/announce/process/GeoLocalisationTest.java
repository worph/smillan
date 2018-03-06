package com.smilan.test.domain.announce.process;

import com.smilan.api.common.manager.option.builder.OptionServiceBuilder;
import com.smilan.api.common.manager.option.builder.OrderBuilder;
import com.smilan.api.common.manager.option.builder.SearchOptionBuilder;
import com.smilan.api.domain.announce.Announce;
import com.smilan.api.domain.announce.AnnounceManager;
import com.smilan.api.domain.announce.AnnounceManagerDTO;
import com.smilan.api.domain.announce.builder.AnnounceBuilder;
import com.smilan.api.domain.announce.builder.AnnounceManagerDTOBuilder;
import com.smilan.api.domain.announce.builder.GeoLocationBuilder;
import com.smilan.logic.domain.chat.XMPPService;
import com.smilan.process.domain.announce.configuration.AnnounceProcessConfiguration;
import com.smilan.test.common.support.configuration.CommonConfiguration;
import com.smilan.test.domain.announce.support.AnnounceDataTest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author Thomas
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CommonConfiguration.class, AnnounceProcessConfiguration.class})
@ActiveProfiles(profiles = {"EmbeddedDatabase", "Hibernate", "EmbeddedH2", "HikariCP"})
@TestPropertySource(properties = {"javax.persistence.schema-generation.database.action=drop-and-create",XMPPService.PPT_SIMULATE_BEHAVIOUR+"=true"})
public class GeoLocalisationTest {

    @Autowired
    private AnnounceManager gererAnnounce;

    public AnnounceManagerDTO defineAnnounce(AnnounceManagerDTO gererAnnounceDTO) {
        int numberOfAnnounce = gererAnnounceDTO.getEntities().size();
        gererAnnounceDTO = this.gererAnnounce.define(gererAnnounceDTO);

        Assert.assertNotNull(gererAnnounceDTO);

        List<Announce> personnes = gererAnnounceDTO.getEntities();
        Assert.assertNotNull(personnes);
        Assert.assertEquals(numberOfAnnounce, personnes.size());
        Assert.assertNotNull(personnes.get(0));

        return gererAnnounceDTO;
    }

    @Test
    public void searchAnnounceByNearestLocationTest() {
        //create data set
        AnnounceDataTest personneDataTest = new AnnounceDataTest();
        AnnounceManagerDTO definedAnnounces = this.defineAnnounce(new AnnounceManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        personneDataTest.builder1().withProfileId("0").withLocations(Arrays.asList(new GeoLocationBuilder().withLat(49.054120f).withLon(2.024066f).build())).build(),
                        personneDataTest.builder1().withProfileId("1").withLocations(Arrays.asList(new GeoLocationBuilder().withLat(49.055048f).withLon(2.015697f).build())).build(),
                        personneDataTest.builder1().withProfileId("2").withLocations(Arrays.asList(new GeoLocationBuilder().withLat(49.053220f).withLon(2.032134f).build())).build(),
                        personneDataTest.builder1().withProfileId("3").withLocations(Arrays.asList(new GeoLocationBuilder().withLat(49.051926f).withLon(2.045953f).build())).build()
                ))
                .build());

        {
            //49.056623, 2.010419 => order 1 0 2 3
            //create expected result
            List<Announce> expectedResult = new ArrayList<>();
            expectedResult.add(definedAnnounces.getEntities().get(1));
            expectedResult.add(definedAnnounces.getEntities().get(0));
            expectedResult.add(definedAnnounces.getEntities().get(2));
            expectedResult.add(definedAnnounces.getEntities().get(3));

            //perform operation
            AnnounceManagerDTO searchedAnnounces = gererAnnounce.search(new AnnounceManagerDTOBuilder()
                    .withOptionService(new OptionServiceBuilder().withSearchOption(
                            new SearchOptionBuilder()
                                    .withOrder(Arrays.asList(
                                            new OrderBuilder()
                                                    .withItem("geodist").withParameters("49.056623,2.010419,100")
                                                    .build()))
                                    .build())
                            .build())
                    .withEntities(Arrays.asList(new AnnounceBuilder().build()))
                    .build());

            //verify operation
            Assert.assertNotNull(searchedAnnounces);
            List<Announce> result = searchedAnnounces.getEntities();
            Assert.assertNotNull(result);
            Assert.assertEquals(expectedResult.size(), result.size());
            for (int i = 0; i < expectedResult.size(); i++) {
                Assert.assertEquals(result.get(i), expectedResult.get(i));
            }
        }
    }

}
