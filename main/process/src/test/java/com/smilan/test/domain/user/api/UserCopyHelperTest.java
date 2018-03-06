package com.smilan.test.domain.user.api;

import com.smilan.api.domain.user.UserCopyHelper;
import com.smilan.api.domain.user.UserManagerDTO;
import com.smilan.api.domain.user.builder.UserManagerDTOBuilder;
import com.smilan.test.domain.user.support.UserDataTest;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Thomas
 *
 */
public class UserCopyHelperTest {

    private final UserCopyHelper userHelper = new UserCopyHelper();

    @Test
    public void copyTest() {
        UserDataTest userDataTest = new UserDataTest();
        UserManagerDTO userSet = new UserManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        userDataTest.builder1().build(),
                        userDataTest.builder2().build()
                )).build();

        Assert.assertEquals(userSet, this.userHelper.copy(userSet));
    }

}
