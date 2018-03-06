package com.smilan.test.domain.announce.api;

import com.smilan.api.domain.announce.AnnounceCopyHelper;
import com.smilan.api.domain.announce.AnnounceManagerDTO;
import com.smilan.api.domain.announce.builder.AnnounceManagerDTOBuilder;
import com.smilan.test.domain.announce.support.AnnounceDataTest;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Thomas
 *
 */
public class AnnounceHelperTest {

    private final AnnounceCopyHelper announceHelper = new AnnounceCopyHelper();

    @Test
    public void copyTest() {
        AnnounceDataTest announceDataTest = new AnnounceDataTest();
        AnnounceManagerDTO announceSet = new AnnounceManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        announceDataTest.builder1().build(),
                        announceDataTest.builder2().build()
                )).build();

        Assert.assertEquals(announceSet, this.announceHelper.copy(announceSet));
    }

}
