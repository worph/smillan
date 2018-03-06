package com.smilan.test.domain.user.process;

import com.smilan.api.common.dto.Contexte;
import com.smilan.api.domain.user.User;
import com.smilan.api.domain.user.UserManager;
import com.smilan.api.domain.user.UserManagerDTO;
import com.smilan.api.domain.user.builder.UserManagerDTOBuilder;
import com.smilan.logic.common.configuration.CommunLogicConfiguration;
import com.smilan.process.domain.profileUser.configuration.ProfileProcessConfiguration;
import com.smilan.test.common.data.CommunDataTest;
import com.smilan.test.common.support.configuration.CommonConfiguration;
import com.smilan.test.domain.user.support.UserDataTest;
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
@ContextConfiguration(classes = {CommonConfiguration.class, ProfileProcessConfiguration.class})
@ActiveProfiles(profiles = { "EmbeddedDatabase", "Hibernate", "EmbeddedH2", "HikariCP" })
@TestPropertySource(properties = { "javax.persistence.schema-generation.database.action=drop-and-create" })
public class DefineUserTest {

    @Autowired
    private UserManager userManager;

    @Test
    public void defineUserTest() {
        UserDataTest userDataTest = new UserDataTest();

        UserManagerDTO userManagerDTO = new UserManagerDTOBuilder()
                .withEntities(Arrays.asList(userDataTest.builder1().withLogin("c1").build()))
                .build();

        User expectedUser = userManagerDTO.getEntities().get(0);

        userManagerDTO = this.userManager.define(userManagerDTO);

        Assert.assertNotNull(userManagerDTO);

        List<User> users = userManagerDTO.getEntities();
        Assert.assertNotNull(users);
        Assert.assertEquals(1, users.size());

        User user = users.get(0);
        expectedUser.setId(user.getId());
        Assert.assertEquals(expectedUser, user);
    }

    @Test
    public void defineUserExistanteTest() {
        UserDataTest userDataTest = new UserDataTest();

        UserManagerDTO userManagerDTO = this.userManager.define(new UserManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        userDataTest.builder1().withLogin("a1").build()                        ))
                .build());

        userManagerDTO = new UserManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        userDataTest.builder2().withLogin("a2").build()))
                .build();

        User expectedUser = userManagerDTO.getEntities().get(0);

        userManagerDTO = this.userManager.define(userManagerDTO);

        Assert.assertNotNull(userManagerDTO);

        List<User> users = userManagerDTO.getEntities();
        Assert.assertNotNull(users);
        Assert.assertEquals(1, users.size());

        User user = users.get(0);
        expectedUser.setId(user.getId());
        Assert.assertEquals(expectedUser, user);
    }

    @Test
    public void multipleDefinirUserTest() {
        UserDataTest userDataTest = new UserDataTest();

        UserManagerDTO userManagerDTO = this.userManager.define(new UserManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        userDataTest.builder1().withLogin("b1").build(),
                        userDataTest.builder1().withLogin("b2").build()                        ))
                .build());

        userManagerDTO = new UserManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        userDataTest.builder1().withLogin("b3").build(),
                        userDataTest.builder1().withLogin("b4").build(),
                        userDataTest.builder1().withLogin("b5").build(),
                        userDataTest.builder2().withLogin("b6").build(),
                        userDataTest.builder2().withLogin("b7").build()))
                .build();

        Iterator<User> usersIterator = userManagerDTO.getEntities().iterator();
        User expectedUser = usersIterator.next();
        User expectedUser2 = usersIterator.next();
        User expectedUser3 = usersIterator.next();
        User expectedUser4 = usersIterator.next();
        User expectedUser5 = usersIterator.next();

        userManagerDTO = this.userManager.define(userManagerDTO);

        Assert.assertNotNull(userManagerDTO);

        List<User> users = userManagerDTO.getEntities();
        Assert.assertNotNull(users);
        Assert.assertEquals(5, users.size());

        usersIterator = users.iterator();
        User user = usersIterator.next();
        expectedUser.setId(user.getId());
        Assert.assertEquals(expectedUser, user);
        User user2 = usersIterator.next();
        expectedUser2.setId(user2.getId());
        Assert.assertEquals(expectedUser2, user2);
        User user3 = usersIterator.next();
        expectedUser3.setId(user3.getId());
        Assert.assertEquals(expectedUser3, user3);
        User user4 = usersIterator.next();
        expectedUser4.setId(user4.getId());
        Assert.assertEquals(expectedUser4, user4);
        User user5 = usersIterator.next();
        expectedUser5.setId(user5.getId());
        Assert.assertEquals(expectedUser5, user5);
    }
}
