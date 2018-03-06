/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.process.domain.profileUser;

import com.smilan.api.common.manager.DTOErrorHelper;
import com.smilan.api.domain.chat.ChatManager;
import com.smilan.api.domain.media.Media;
import com.smilan.api.domain.media.MediaManager;
import com.smilan.api.domain.profile.Profile;
import com.smilan.api.domain.profile.ProfileManager;
import com.smilan.api.domain.profile.ProfileManagerDTO;
import com.smilan.api.domain.profile.builder.ProfileBuilder;
import com.smilan.api.domain.profile.builder.ProfileManagerDTOBuilder;
import com.smilan.api.domain.profile.AnonymousUserGenerator;
import com.smilan.api.domain.user.User;
import com.smilan.api.domain.user.UserManager;
import com.smilan.api.domain.user.UserManagerDTO;
import com.smilan.api.domain.user.UserUpgradePackage;
import com.smilan.api.domain.user.builder.UserBuilder;
import com.smilan.api.domain.user.builder.UserManagerDTOBuilder;
import com.smilan.logic.domain.security.Realm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author pierr
 */
public class AnonymousUserCreation implements AnonymousUserGenerator {

    private final UserManager userManager;
    private final ProfileManager profileManager;
    private final MediaManager mediaManager;
    private final ChatManager chatManager;
    private final Random random = new Random();


    public AnonymousUserCreation(UserManager userManager, ProfileManager profileManager, MediaManager mediaManager, ChatManager chatManager) {
        this.userManager = userManager;
        this.profileManager = profileManager;
        this.mediaManager = mediaManager;
        this.chatManager = chatManager;
    }
    
    @Transactional
    @Override
    public UserManagerDTO createNewUser() {
        List<User> users = new ArrayList<>();
        final User build = new UserBuilder()
                .withId(null)
                .withEmail("")//no email for new user
                .withLogin(RandomStringUtils.randomAlphanumeric(64))
                .withPassword(RandomStringUtils.randomAlphanumeric(32))
                .withXmppPassword(RandomStringUtils.randomAlphanumeric(32))
                .withAnonymous(true)
                .withApiKey(RandomStringUtils.randomAlphanumeric(32))
                .withRoles(new HashSet<>(Arrays.asList(Realm.ROLE_USER,Realm.ROLE_USER_ANONYMOUS)))
                .build();
        users.add(build);
        final UserManagerDTO define = userManager.define(new UserManagerDTOBuilder().withEntities(users).build());
        createNewProfile(define.getEntities().get(0).getId());
        return define;
    }

    @Transactional
    @Override
    public UserManagerDTO upgradeProfile(UserUpgradePackage upgradePackage) {
        // read user        
        UserManagerDTO gererUserDTO = userManager.search(new UserManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        new UserBuilder()
                                .withId(upgradePackage.getUserID()).build()))
                .build());
        // read user profile       
        ProfileManagerDTO profileManagerDTO = profileManager.search(
                new ProfileManagerDTOBuilder()
                        .withEntities(
                                Arrays.asList(new ProfileBuilder().withUserId(upgradePackage.getUserID()).build())).build());
        //upgrade profile
        if (profileManagerDTO.getEntities().size() != 1) {
            return DTOErrorHelper.makeError(new UserManagerDTOBuilder().build(), "ERR_USER_NOT_ANNONYMOUS_A");
        }
        if (gererUserDTO.getEntities().size() != 1) {
            return DTOErrorHelper.makeError(new UserManagerDTOBuilder().build(), "ERR_USER_ID_NOT_EXSIST");
        }
        final User user = gererUserDTO.getEntities().get(0);
        String oldLogin = user.getLogin();
        String oldPassword = user.getPassword();
        final Profile profile = profileManagerDTO.getEntities().get(0);
        if (!user.isAnonymous()) {
            return DTOErrorHelper.makeError(new UserManagerDTOBuilder().build(), "ERR_USER_NOT_ANNONYMOUS_B");
        }
        UserManagerDTO gererUserDTO2 = userManager.search(new UserManagerDTOBuilder()
                .withEntities(Arrays.asList(
                        new UserBuilder()
                                .withLogin(upgradePackage.getLogin())
                                .build()))
                .build());
        if (!gererUserDTO2.getEntities().isEmpty()) {
            return DTOErrorHelper.makeError(new UserManagerDTOBuilder().build(), "ERR_LOGIN_ALREADY_TAKEN");
        }
        
        //this method actualy update login password
        userManager.changeLoginPassword(upgradePackage.getLogin(), upgradePackage.getPassword(),oldLogin,oldPassword);
        user.setLogin(null);//means we wont update it with regular process
        user.setPassword(null);//means we wont update it with regular process
        
        //update  other stuff as needed
        user.setAnonymous(false);
        profile.setProfileName(upgradePackage.getDisplayName());

        //save user
        profileManager.define(profileManagerDTO);
        return userManager.define(gererUserDTO);
    }

    @Transactional
    @Override
    public ProfileManagerDTO createNewProfile(String userId) {
        /**
         * retreive users in bdd
         */
        final List<Media> defaultAvatarList = mediaManager.getDefaultAvatarList();
        final List<Media> defaultBackGroundList = mediaManager.getDefaultBackGroundList();
        final User result = userManager.search(new UserManagerDTOBuilder().withEntities(Arrays.asList(new UserBuilder()
                .withId(userId)
                .build())).build()).getEntities().get(0);
        String xmppPassword = result.getXmppPassword();
        final String randomUserName = "Anonymous-" + random.nextInt(1000);
        String xmppxuer = RandomStringUtils.randomAlphanumeric(64);
        final String chatId = chatManager.createUser(xmppxuer, xmppPassword);
        final Media avatar = defaultAvatarList.get(random.nextInt(defaultAvatarList.size()));
        final Media background = defaultBackGroundList.get(random.nextInt(defaultBackGroundList.size()));
        avatar.setId(null);//TODO why can't reuse the Media???
        background.setId(null);
        List<Profile> profiles = new ArrayList<>();
        final Profile build2 = new ProfileBuilder()
                .withId(null)
                .withUserId(userId)
                .withProfileName(randomUserName)
                .withXmppId(chatId)
                .withDescription("")
                .withAvatar(avatar)
                .withBackground(background)
                .build();
        profiles.add(build2);
        return profileManager.define(new ProfileManagerDTOBuilder().withEntities(profiles).build());
    }
}
