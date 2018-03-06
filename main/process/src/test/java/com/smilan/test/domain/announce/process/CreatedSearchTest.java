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
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class CreatedSearchTest {

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
    public void searchbyCreatedDateAscTest() { 
        
        List<Announce> expectedAscResult = new ArrayList<>();
        List<Announce> expectedDescResult = new ArrayList<>();
        //create data set
        {
        AnnounceDataTest personneDataTest = new AnnounceDataTest();
        AnnounceManagerDTO definedAnnounces = this.defineAnnounce(new AnnounceManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        personneDataTest.builder1().withProfileId("0").build()
                ))
                .build());
            expectedAscResult.add(definedAnnounces.getEntities().get(0));
            expectedDescResult.add(0,definedAnnounces.getEntities().get(0));
        }
        try {
            Thread.sleep(200);//each creation is separated
        } catch (InterruptedException ex) {
            Logger.getLogger(CreatedSearchTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        {
        AnnounceDataTest personneDataTest = new AnnounceDataTest();
        AnnounceManagerDTO definedAnnounces = this.defineAnnounce(new AnnounceManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        personneDataTest.builder1().withProfileId("1").build()
                ))
                .build());
            expectedAscResult.add(definedAnnounces.getEntities().get(0));
            expectedDescResult.add(0,definedAnnounces.getEntities().get(0));
        }
        try {
            Thread.sleep(200);//each creation is separated
        } catch (InterruptedException ex) {
            Logger.getLogger(CreatedSearchTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        {
        AnnounceDataTest personneDataTest = new AnnounceDataTest();
        AnnounceManagerDTO definedAnnounces = this.defineAnnounce(new AnnounceManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        personneDataTest.builder1().withProfileId("2").build()
                ))
                .build());
            expectedAscResult.add(definedAnnounces.getEntities().get(0));
            expectedDescResult.add(0,definedAnnounces.getEntities().get(0));
        }
        try {
            Thread.sleep(200);//each creation is separated by
        } catch (InterruptedException ex) {
            Logger.getLogger(CreatedSearchTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        {
        AnnounceDataTest personneDataTest = new AnnounceDataTest();
        AnnounceManagerDTO definedAnnounces = this.defineAnnounce(new AnnounceManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        personneDataTest.builder1().withProfileId("3").build()
                ))
                .build());
        
            expectedAscResult.add(definedAnnounces.getEntities().get(0));
            expectedDescResult.add(0,definedAnnounces.getEntities().get(0));
        }

        //asc
        {
            //create expected result

            //perform operation
            AnnounceManagerDTO searchedAnnounces = gererAnnounce.search(new AnnounceManagerDTOBuilder()
                    .withOptionService(new OptionServiceBuilder().withSearchOption(
                            new SearchOptionBuilder()
                                    .withOrder(Arrays.asList(
                                            new OrderBuilder()
                                                    .withItem("created").build()))
                                    .build())
                            .build())
                    .withEntities(Arrays.asList(new AnnounceBuilder().build()))
                    .build());

            //verify operation
            Assert.assertNotNull(searchedAnnounces);
            List<Announce> result = searchedAnnounces.getEntities();
            Assert.assertNotNull(result);
            Assert.assertEquals(expectedAscResult.size(), result.size());
            for (int i = 0; i < expectedAscResult.size(); i++) {
                Assert.assertEquals(result.get(i), expectedAscResult.get(i));
            }
        }
        
        //desc
        {
            //create expected result

            //perform operation
            AnnounceManagerDTO searchedAnnounces = gererAnnounce.search(new AnnounceManagerDTOBuilder()
                    .withOptionService(new OptionServiceBuilder().withSearchOption(
                            new SearchOptionBuilder()
                                    .withOrder(Arrays.asList(
                                            new OrderBuilder()
                                                    .withItem("created").withDirection("desc").build()))
                                    .build())
                            .build())
                    .withEntities(Arrays.asList(new AnnounceBuilder().build()))
                    .build());

            //verify operation
            Assert.assertNotNull(searchedAnnounces);
            List<Announce> result = searchedAnnounces.getEntities();
            Assert.assertNotNull(result);
            Assert.assertEquals(expectedDescResult.size(), result.size());
            for (int i = 0; i < expectedDescResult.size(); i++) {
                Assert.assertEquals(result.get(i), expectedDescResult.get(i));
            }
        }
    }
    
}
