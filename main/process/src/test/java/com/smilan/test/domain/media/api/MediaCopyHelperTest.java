package com.smilan.test.domain.media.api;

import com.smilan.api.domain.media.MediaCopyHelper;
import com.smilan.api.domain.media.MediaManagerDTO;
import com.smilan.api.domain.media.builder.MediaManagerDTOBuilder;
import com.smilan.test.domain.media.support.MediaDataTest;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Thomas
 *
 */
public class MediaCopyHelperTest {

    private final MediaCopyHelper mediaHelper = new MediaCopyHelper();

    @Test
    public void copyTest() {
        MediaDataTest mediaDataTest = new MediaDataTest();
        MediaManagerDTO mediaSet = new MediaManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        mediaDataTest.builder1().build(),
                        mediaDataTest.builder2().build()
                )).build();

        Assert.assertEquals(mediaSet, this.mediaHelper.copy(mediaSet));
    }

}
