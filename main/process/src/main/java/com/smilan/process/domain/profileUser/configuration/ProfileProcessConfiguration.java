package com.smilan.process.domain.profileUser.configuration;

import com.smilan.api.domain.chat.ChatManager;
import com.smilan.api.domain.media.MediaManager;
import com.smilan.api.domain.profile.AnonymousUserGenerator;
import com.smilan.api.domain.profile.ProfileManager;
import com.smilan.api.domain.user.UserManager;
import com.smilan.logic.domain.profile.ProfileLogic;
import com.smilan.logic.domain.profile.configuration.ProfileLogicConfiguration;
import com.smilan.logic.domain.security.Realm;
import com.smilan.logic.domain.user.UserLogic;
import com.smilan.logic.domain.user.configuration.UserLogicConfiguration;
import com.smilan.process.domain.chat.configuration.ChatConfiguration;
import com.smilan.process.domain.media.configuration.MediaProcessConfiguration;
import com.smilan.process.domain.profileUser.AnonymousUserCreation;
import com.smilan.process.domain.profileUser.builder.DefaultProfileManagerBuilder;
import com.smilan.process.domain.profileUser.builder.DefaultUserManagerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Thomas
 *
 */
@Configuration
@Import({ProfileLogicConfiguration.class, UserLogicConfiguration.class, MediaProcessConfiguration.class, ChatConfiguration.class})
public class ProfileProcessConfiguration {

    @Bean
    public ProfileManager profileManager(
            ProfileLogic profileLogic,
            Realm customRealm
    ) {
        return new DefaultProfileManagerBuilder().withProfileLogic(profileLogic).withRealm(customRealm).build();
    }

    @Bean
    public UserManager userManager(
            UserLogic userLogic,
            Realm customRealm
    ) {
        return new DefaultUserManagerBuilder().withUserLogic(userLogic).withRealm(customRealm).build();
    }

    @Bean
    public AnonymousUserGenerator anonymousUserGenerator(
            UserManager userManager, 
            ProfileManager profileManager, 
            MediaManager mediaManager, 
            ChatManager chatManager
    ) {
        return new AnonymousUserCreation(userManager, profileManager, mediaManager, chatManager);
    }

}
