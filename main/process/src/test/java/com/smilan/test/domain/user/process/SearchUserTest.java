package com.smilan.test.domain.user.process;

import com.smilan.api.common.dto.Contexte;
import com.smilan.api.domain.user.User;
import com.smilan.api.domain.user.UserManager;
import com.smilan.api.domain.user.UserManagerDTO;
import com.smilan.api.domain.user.builder.UserBuilder;
import com.smilan.api.domain.user.builder.UserManagerDTOBuilder;
import com.smilan.logic.common.configuration.CommunLogicConfiguration;
import com.smilan.process.domain.profileUser.configuration.ProfileProcessConfiguration;
import com.smilan.test.common.data.CommunDataTest;
import com.smilan.test.common.support.configuration.CommonConfiguration;
import com.smilan.test.domain.user.support.UserDataTest;
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
@ContextConfiguration(classes = {CommonConfiguration.class, ProfileProcessConfiguration.class})
@ActiveProfiles(profiles = {"EmbeddedDatabase", "Hibernate", "EmbeddedH2", "HikariCP"})
@TestPropertySource(properties = {"javax.persistence.schema-generation.database.action=drop-and-create"})
public class SearchUserTest {

    @Autowired
    private UserManager gererUser;

    public UserManagerDTO defineUser(UserManagerDTO gererUserDTO) {
        int numberOfUser = gererUserDTO.getEntities().size();
        gererUserDTO = this.gererUser.define(gererUserDTO);

        Assert.assertNotNull(gererUserDTO);

        List<User> personnes = gererUserDTO.getEntities();
        Assert.assertNotNull(personnes);
        Assert.assertEquals(numberOfUser, personnes.size());
        Assert.assertNotNull(personnes.get(0));

        return gererUserDTO;
    }

    @Test
    public void searchUserByIdentifiantTest() {
        UserDataTest personneDataTest = new UserDataTest();
        UserManagerDTO gererUserDTO = this.defineUser(new UserManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        personneDataTest.builder1().build()))
                .build());

        User personneExpected = gererUserDTO.getEntities().get(0);

        gererUserDTO = SearchUserTest.this.gererUser.search(new UserManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        new UserBuilder()
                        .withId(gererUserDTO.getEntities().get(0).getId()).build()))
                .build());

        Assert.assertNotNull(gererUserDTO);

        List<User> personnes = gererUserDTO.getEntities();
        Assert.assertNotNull(personnes);
        Assert.assertEquals(1, personnes.size());

        User personne = personnes.get(0);
        Assert.assertEquals(personneExpected, personne);
    }

    @Test
    public void searchUserByLogin() {
        UserDataTest personneDataTest = new UserDataTest();

        UserManagerDTO gererUserDTO = this.defineUser(new UserManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        personneDataTest.builder1().withLogin("ztitle").build()))
                .build());

        User personneExpected = gererUserDTO.getEntities().get(0);

        gererUserDTO = SearchUserTest.this.gererUser.search(new UserManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        new UserBuilder().withLogin("ztitle").build()))
                .build());

        Assert.assertNotNull(gererUserDTO);

        List<User> personnes = gererUserDTO.getEntities();
        Assert.assertNotNull(personnes);
        Assert.assertEquals(1, personnes.size());

        User personne = personnes.get(0);
        Assert.assertEquals(personneExpected, personne);
    }
}
