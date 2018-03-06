/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.smilan.process.commun.support.configuration;

import com.smilan.api.domain.announce.AnnounceManager;
import com.smilan.api.domain.chat.ChatManager;
import com.smilan.api.domain.media.MediaManager;
import com.smilan.api.domain.profile.AnonymousUserGenerator;
import com.smilan.api.domain.profile.ProfileManager;
import com.smilan.api.domain.user.UserManager;
import com.smilan.process.domain.security.AdministrationExcutor;
import com.smilan.logic.domain.security.configuration.RealmConfiguration;
import com.smilan.process.commun.support.DummyPopulator;
import com.smilan.process.domain.announce.configuration.AnnounceProcessConfiguration;
import com.smilan.process.domain.category.configuration.CategoryProcessConfiguration;
import com.smilan.process.domain.chat.configuration.ChatConfiguration;
import com.smilan.process.domain.media.configuration.MediaProcessConfiguration;
import com.smilan.process.domain.profileUser.configuration.ProfileProcessConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 *
 * @author pierr
 */
@Configuration
@Import({
    AnnounceProcessConfiguration.class,
    ProfileProcessConfiguration.class,
    MediaProcessConfiguration.class,
    ChatConfiguration.class,
    RealmConfiguration.class
})
public class DummyPopulatorConfiguration {

    @Bean
    public DummyPopulator dummyPopulator(
            AnnounceManager announceManager, 
            ProfileManager profileManager, 
            MediaManager mediaManager, 
            AnonymousUserGenerator anonymousUserGenerator, 
            AdministrationExcutor administrationExcutor) {
        return new DummyPopulator(announceManager, profileManager, mediaManager, anonymousUserGenerator, administrationExcutor);
    }
}
