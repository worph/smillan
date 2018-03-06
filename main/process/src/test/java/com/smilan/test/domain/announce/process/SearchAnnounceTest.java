package com.smilan.test.domain.announce.process;

import com.smilan.api.domain.announce.Announce;
import com.smilan.api.domain.announce.AnnounceManager;
import com.smilan.api.domain.announce.AnnounceManagerDTO;
import com.smilan.api.domain.announce.builder.AnnounceBuilder;
import com.smilan.api.domain.announce.builder.AnnounceManagerDTOBuilder;
import com.smilan.api.domain.category.Category;
import com.smilan.api.domain.category.builder.CategoryBuilder;
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
public class SearchAnnounceTest {

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
    public void searchAnnounceMoraleByIdentifiantTest() {
        AnnounceDataTest personneDataTest = new AnnounceDataTest();
        AnnounceManagerDTO gererAnnounceDTO = this.defineAnnounce(new AnnounceManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        personneDataTest.builder1().build()))
                .build());

        Announce personneExpected = gererAnnounceDTO.getEntities().get(0);

        gererAnnounceDTO = SearchAnnounceTest.this.gererAnnounce.search(new AnnounceManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        new AnnounceBuilder()
                                .withId(gererAnnounceDTO.getEntities().get(0).getId()).build()))
                .build());

        Assert.assertNotNull(gererAnnounceDTO);

        List<Announce> personnes = gererAnnounceDTO.getEntities();
        Assert.assertNotNull(personnes);
        Assert.assertEquals(1, personnes.size());

        Announce personne = personnes.get(0);
        Assert.assertEquals(personneExpected, personne);
    }
    
    public static List<Category> makeCategories(String ... categories){
        List<Category> ret = new ArrayList<>();
        for (String category : categories) {
            ret.add(new CategoryBuilder().withValue(category).build());
        }
        return ret;
    }

    @Test
    public void searchAnnounceByCategory() {
        //create data set
        AnnounceDataTest personneDataTest = new AnnounceDataTest();
        AnnounceManagerDTO definedAnnounces = this.defineAnnounce(new AnnounceManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        personneDataTest.builder1().withCategories(makeCategories("a", "b", "c")).build(),
                        personneDataTest.builder1().withCategories(makeCategories("b", "c")).build(),
                        personneDataTest.builder1().withCategories(makeCategories("a", "c")).build(),
                        personneDataTest.builder1().withCategories(makeCategories("c")).build(),
                        personneDataTest.builder1().withCategories(makeCategories("a", "b")).build(),
                        personneDataTest.builder1().withCategories(makeCategories("b")).build(),
                        personneDataTest.builder1().withCategories(makeCategories("a")).build(),
                        personneDataTest.builder1().withCategories(makeCategories()).build()
                ))
                .build());

        {
            //create expected result
            List<Announce> expectedResult = new ArrayList<>();
            expectedResult.add(definedAnnounces.getEntities().get(0));
            expectedResult.add(definedAnnounces.getEntities().get(2));
            expectedResult.add(definedAnnounces.getEntities().get(4));
            expectedResult.add(definedAnnounces.getEntities().get(6));

            //perform operation
            AnnounceManagerDTO searchedAnnounces = SearchAnnounceTest.this.gererAnnounce.search(new AnnounceManagerDTOBuilder()
                    .withEntities(Arrays.asList(
                            new AnnounceBuilder().withCategories(makeCategories("a")).build()))
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

        {
            //create expected result
            List<Announce> expectedResult = new ArrayList<>();
            expectedResult.add(definedAnnounces.getEntities().get(0));
            expectedResult.add(definedAnnounces.getEntities().get(4));

            //perform operation
            AnnounceManagerDTO searchedAnnounces = SearchAnnounceTest.this.gererAnnounce.search(new AnnounceManagerDTOBuilder()
                    .withEntities(Arrays.asList(
                            new AnnounceBuilder().withCategories(makeCategories("a", "b")).build()))
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

    @Test
    public void searchAnnounceByTitle() {
        AnnounceDataTest personneDataTest = new AnnounceDataTest();

        AnnounceManagerDTO gererAnnounceDTO = this.defineAnnounce(new AnnounceManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        personneDataTest.builder1().withTitle("ztitle").build(),
                        personneDataTest.builder1().withTitle("ztitle2").build(),
                        personneDataTest.builder1().withTitle("ztitle4").build()
                ))
                .build());

        Announce personneExpected = gererAnnounceDTO.getEntities().get(0);

        gererAnnounceDTO = SearchAnnounceTest.this.gererAnnounce.search(new AnnounceManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        new AnnounceBuilder().withTitle("ztitle").build()))
                .build());

        Assert.assertNotNull(gererAnnounceDTO);

        List<Announce> personnes = gererAnnounceDTO.getEntities();
        Assert.assertNotNull(personnes);
        Assert.assertEquals(1, personnes.size());

        Announce personne = personnes.get(0);
        Assert.assertEquals(personneExpected, personne);
    }
}
