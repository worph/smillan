package com.smilan.process.domain.announce.configuration;

import com.smilan.api.domain.announce.AnnounceManager;
import com.smilan.logic.domain.announce.AnnounceLogic;
import com.smilan.logic.domain.announce.configuration.AnnounceLogicConfiguration;
import com.smilan.logic.domain.category.CategoryLogic;
import com.smilan.logic.domain.category.configuration.CategoryLogicConfiguration;
import com.smilan.logic.domain.chat.XMPPService;
import com.smilan.logic.domain.chat.configuration.ChatLogicConfiguration;
import com.smilan.process.domain.security.AdministrationExcutor;
import com.smilan.logic.domain.security.Realm;
import com.smilan.logic.domain.security.configuration.RealmConfiguration;
import com.smilan.process.domain.announce.builder.DefaultAnnounceManagerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author Thomas
 *
 */
@Configuration
@Import({
    AnnounceLogicConfiguration.class,
    CategoryLogicConfiguration.class,
    ChatLogicConfiguration.class,
    RealmConfiguration.class
})
public class AnnounceProcessConfiguration {

    @Bean
    public AnnounceManager announceManager(
            AnnounceLogic announceLogic,
            CategoryLogic categoryLogic,
            Realm customRealm,
            XMPPService xmppService) {
        return new DefaultAnnounceManagerBuilder()
                .withAnnounceLogic(announceLogic)
                .withRealm(customRealm)
                .withCategoryLogic(categoryLogic)
                .withXmppService(xmppService)
                .build();
    }
    
}
