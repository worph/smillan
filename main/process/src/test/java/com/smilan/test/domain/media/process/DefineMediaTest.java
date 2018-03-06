package com.smilan.test.domain.media.process;

import com.smilan.api.common.dto.Contexte;
import com.smilan.api.domain.media.Media;
import com.smilan.api.domain.media.MediaManager;
import com.smilan.api.domain.media.MediaManagerDTO;
import com.smilan.api.domain.media.builder.MediaManagerDTOBuilder;
import com.smilan.logic.common.configuration.CommunLogicConfiguration;
import com.smilan.process.domain.category.configuration.CategoryProcessConfiguration;
import com.smilan.process.domain.media.configuration.MediaProcessConfiguration;
import com.smilan.test.common.data.CommunDataTest;
import com.smilan.test.common.support.configuration.CommonConfiguration;
import com.smilan.test.domain.media.support.MediaDataTest;
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
 *         TODO TEST on lock, intigrity
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CommonConfiguration.class, MediaProcessConfiguration.class})
@ActiveProfiles(profiles = { "EmbeddedDatabase", "Hibernate", "EmbeddedH2", "HikariCP" })
@TestPropertySource(properties = { "javax.persistence.schema-generation.database.action=drop-and-create" })
public class DefineMediaTest {

    @Autowired
    private MediaManager mediaManager;

    @Test
    public void defineMediaTest() {
        MediaDataTest mediaDataTest = new MediaDataTest();

        MediaManagerDTO mediaManagerDTO = new MediaManagerDTOBuilder()
                .withEntities(Arrays.asList(mediaDataTest.builder1().build()))
                .build();

        Media expectedMedia = mediaManagerDTO.getEntities().get(0);

        mediaManagerDTO = this.mediaManager.define(mediaManagerDTO);

        Assert.assertNotNull(mediaManagerDTO);

        List<Media> medias = mediaManagerDTO.getEntities();
        Assert.assertNotNull(medias);
        Assert.assertEquals(1, medias.size());

        Media media = medias.get(0);
        expectedMedia.setId(media.getId());
        Assert.assertEquals(expectedMedia, media);
    }

    @Test
    public void defineMediaExistanteTest() {
        MediaDataTest mediaDataTest = new MediaDataTest();

        MediaManagerDTO mediaManagerDTO = this.mediaManager.define(new MediaManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        mediaDataTest.builder1().build()                        ))
                .build());

        mediaManagerDTO = new MediaManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        mediaDataTest.builder2().build()))
                .build();

        Media expectedMedia = mediaManagerDTO.getEntities().get(0);

        mediaManagerDTO = this.mediaManager.define(mediaManagerDTO);

        Assert.assertNotNull(mediaManagerDTO);

        List<Media> medias = mediaManagerDTO.getEntities();
        Assert.assertNotNull(medias);
        Assert.assertEquals(1, medias.size());

        Media media = medias.get(0);
        expectedMedia.setId(media.getId());
        Assert.assertEquals(expectedMedia, media);
    }

    @Test
    public void multipleDefinirMediaTest() {
        MediaDataTest mediaDataTest = new MediaDataTest();

        MediaManagerDTO mediaManagerDTO = this.mediaManager.define(new MediaManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        mediaDataTest.builder1().build(),
                        mediaDataTest.builder1().build()                        ))
                .build());

        mediaManagerDTO = new MediaManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        mediaDataTest.builder1().build(),
                        mediaDataTest.builder1().build(),
                        mediaDataTest.builder1().build(),
                        mediaDataTest.builder2().build(),
                        mediaDataTest.builder2().build()))
                .build();

        Iterator<Media> mediasIterator = mediaManagerDTO.getEntities().iterator();
        Media expectedMedia = mediasIterator.next();
        Media expectedMedia2 = mediasIterator.next();
        Media expectedMedia3 = mediasIterator.next();
        Media expectedMedia4 = mediasIterator.next();
        Media expectedMedia5 = mediasIterator.next();

        mediaManagerDTO = this.mediaManager.define(mediaManagerDTO);

        Assert.assertNotNull(mediaManagerDTO);

        List<Media> medias = mediaManagerDTO.getEntities();
        Assert.assertNotNull(medias);
        Assert.assertEquals(5, medias.size());

        mediasIterator = medias.iterator();
        Media media = mediasIterator.next();
        expectedMedia.setId(media.getId());
        Assert.assertEquals(expectedMedia, media);
        Media media2 = mediasIterator.next();
        expectedMedia2.setId(media2.getId());
        Assert.assertEquals(expectedMedia2, media2);
        Media media3 = mediasIterator.next();
        expectedMedia3.setId(media3.getId());
        Assert.assertEquals(expectedMedia3, media3);
        Media media4 = mediasIterator.next();
        expectedMedia4.setId(media4.getId());
        Assert.assertEquals(expectedMedia4, media4);
        Media media5 = mediasIterator.next();
        expectedMedia5.setId(media5.getId());
        Assert.assertEquals(expectedMedia5, media5);
    }
}
