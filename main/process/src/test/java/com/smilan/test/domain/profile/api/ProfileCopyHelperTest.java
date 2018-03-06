package com.smilan.test.domain.profile.api;

import com.smilan.api.domain.profile.ProfileCopyHelper;
import com.smilan.api.domain.profile.ProfileManagerDTO;
import com.smilan.api.domain.profile.builder.ProfileManagerDTOBuilder;
import com.smilan.test.domain.profile.support.ProfileDataTest;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Thomas
 *
 */
public class ProfileCopyHelperTest {

    private final ProfileCopyHelper profileHelper = new ProfileCopyHelper();

    @Test
    public void copyTest() {
        ProfileDataTest profileDataTest = new ProfileDataTest();
        ProfileManagerDTO profileSet = new ProfileManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        profileDataTest.builder1().build(),
                        profileDataTest.builder2().build()
                )).build();

        Assert.assertEquals(profileSet, this.profileHelper.copy(profileSet));
    }

}
