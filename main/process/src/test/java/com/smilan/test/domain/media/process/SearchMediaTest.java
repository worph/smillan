package com.smilan.test.domain.media.process;

import com.smilan.api.common.dto.Contexte;
import com.smilan.api.domain.media.Media;
import com.smilan.api.domain.media.MediaManager;
import com.smilan.api.domain.media.MediaManagerDTO;
import com.smilan.api.domain.media.builder.MediaBuilder;
import com.smilan.api.domain.media.builder.MediaManagerDTOBuilder;
import com.smilan.logic.common.configuration.CommunLogicConfiguration;
import com.smilan.process.domain.media.configuration.MediaProcessConfiguration;
import com.smilan.test.common.data.CommunDataTest;
import com.smilan.test.common.support.configuration.CommonConfiguration;
import com.smilan.test.domain.media.support.MediaDataTest;
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
@ContextConfiguration(classes = {CommonConfiguration.class, MediaProcessConfiguration.class})
@ActiveProfiles(profiles = {"EmbeddedDatabase", "Hibernate", "EmbeddedH2", "HikariCP"})
@TestPropertySource(properties = {"javax.persistence.schema-generation.database.action=drop-and-create"})
@Configuration
public class SearchMediaTest {

    @Autowired
    private MediaManager gererMedia;

    public MediaManagerDTO defineMedia(MediaManagerDTO gererMediaDTO) {
        int numberOfMedia = gererMediaDTO.getEntities().size();
        gererMediaDTO = this.gererMedia.define(gererMediaDTO);

        Assert.assertNotNull(gererMediaDTO);

        List<Media> personnes = gererMediaDTO.getEntities();
        Assert.assertNotNull(personnes);
        Assert.assertEquals(numberOfMedia, personnes.size());
        Assert.assertNotNull(personnes.get(0));

        return gererMediaDTO;
    }

    @Test
    public void searchMediaByIdentifiantTest() {
        MediaDataTest personneDataTest = new MediaDataTest();
        MediaManagerDTO gererMediaDTO = this.defineMedia(new MediaManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        personneDataTest.builder1().build()))
                .build());

        Media personneExpected = gererMediaDTO.getEntities().get(0);

        gererMediaDTO = SearchMediaTest.this.gererMedia.search(new MediaManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        new MediaBuilder()
                        .withId(gererMediaDTO.getEntities().get(0).getId()).build()))
                .build());

        Assert.assertNotNull(gererMediaDTO);

        List<Media> personnes = gererMediaDTO.getEntities();
        Assert.assertNotNull(personnes);
        Assert.assertEquals(1, personnes.size());

        Media personne = personnes.get(0);
        Assert.assertEquals(personneExpected, personne);
    }

}
