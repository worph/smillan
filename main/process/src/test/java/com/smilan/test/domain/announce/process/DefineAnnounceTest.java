package com.smilan.test.domain.announce.process;

import com.smilan.api.domain.announce.Announce;
import com.smilan.api.domain.announce.AnnounceManager;
import com.smilan.api.domain.announce.AnnounceManagerDTO;
import com.smilan.api.domain.announce.builder.AnnounceManagerDTOBuilder;
import com.smilan.logic.domain.chat.XMPPService;
import com.smilan.process.domain.announce.configuration.AnnounceProcessConfiguration;
import com.smilan.test.common.support.configuration.CommonConfiguration;
import com.smilan.test.domain.announce.support.AnnounceDataTest;
import java.util.Arrays;
import java.util.Iterator;
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
 * TODO TEST on lock, intigrity
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CommonConfiguration.class, AnnounceProcessConfiguration.class})
@ActiveProfiles(profiles = {"EmbeddedDatabase", "Hibernate", "EmbeddedH2", "HikariCP"})
@TestPropertySource(properties = {"javax.persistence.schema-generation.database.action=drop-and-create",XMPPService.PPT_SIMULATE_BEHAVIOUR+"=true"})
public class DefineAnnounceTest {

    @Autowired
    private AnnounceManager announceManager;
            
    @Test
    public void defineAnnounceTest() {
        
        AnnounceDataTest announceDataTest = new AnnounceDataTest();

        AnnounceManagerDTO announceManagerDTO = new AnnounceManagerDTOBuilder()
                .withEntities(Arrays.asList(announceDataTest.builder1().build()))
                .build();

        Announce expectedAnnounce = announceManagerDTO.getEntities().get(0);

        announceManagerDTO = this.announceManager.define(announceManagerDTO);

        Assert.assertNotNull(announceManagerDTO);

        List<Announce> announces = announceManagerDTO.getEntities();
        Assert.assertNotNull(announces);
        Assert.assertEquals(1, announces.size());

        Announce announce = announces.get(0);
        expectedAnnounce.setId(announce.getId());
        Assert.assertEquals(expectedAnnounce, announce);
    }

    @Test
    public void defineAnnounceExistanteTest() {
        AnnounceDataTest announceDataTest = new AnnounceDataTest();

        AnnounceManagerDTO announceManagerDTO = this.announceManager.define(new AnnounceManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        announceDataTest.builder1().build()))
                .build());

        announceManagerDTO = new AnnounceManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        announceDataTest.builder2().build()))
                .build();

        Announce expectedAnnounce = announceManagerDTO.getEntities().get(0);

        announceManagerDTO = this.announceManager.define(announceManagerDTO);

        Assert.assertNotNull(announceManagerDTO);

        List<Announce> announces = announceManagerDTO.getEntities();
        Assert.assertNotNull(announces);
        Assert.assertEquals(1, announces.size());

        Announce announce = announces.get(0);
        expectedAnnounce.setId(announce.getId());
        Assert.assertEquals(expectedAnnounce, announce);
    }

    @Test
    public void multipleDefinirAnnounceTest() {
        AnnounceDataTest announceDataTest = new AnnounceDataTest();

        AnnounceManagerDTO announceManagerDTO = this.announceManager.define(new AnnounceManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        announceDataTest.builder1().build(),
                        announceDataTest.builder1().build()))
                .build());

        announceManagerDTO = new AnnounceManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        announceDataTest.builder1().build(),
                        announceDataTest.builder1().build(),
                        announceDataTest.builder1().build(),
                        announceDataTest.builder2().build(),
                        announceDataTest.builder2().build()))
                .build();

        Iterator<Announce> announcesIterator = announceManagerDTO.getEntities().iterator();
        Announce expectedAnnounce = announcesIterator.next();
        Announce expectedAnnounce2 = announcesIterator.next();
        Announce expectedAnnounce3 = announcesIterator.next();
        Announce expectedAnnounce4 = announcesIterator.next();
        Announce expectedAnnounce5 = announcesIterator.next();

        announceManagerDTO = this.announceManager.define(announceManagerDTO);

        Assert.assertNotNull(announceManagerDTO);

        List<Announce> announces = announceManagerDTO.getEntities();
        Assert.assertNotNull(announces);
        Assert.assertEquals(5, announces.size());

        announcesIterator = announces.iterator();
        Announce announce = announcesIterator.next();
        expectedAnnounce.setId(announce.getId());
        Assert.assertEquals(expectedAnnounce, announce);
        Announce announce2 = announcesIterator.next();
        expectedAnnounce2.setId(announce2.getId());
        Assert.assertEquals(expectedAnnounce2, announce2);
        Announce announce3 = announcesIterator.next();
        expectedAnnounce3.setId(announce3.getId());
        Assert.assertEquals(expectedAnnounce3, announce3);
        Announce announce4 = announcesIterator.next();
        expectedAnnounce4.setId(announce4.getId());
        Assert.assertEquals(expectedAnnounce4, announce4);
        Announce announce5 = announcesIterator.next();
        expectedAnnounce5.setId(announce5.getId());
        Assert.assertEquals(expectedAnnounce5, announce5);
    }
}
