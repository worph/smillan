package com.smilan.process.commun.configuration;

import com.smilan.process.commun.support.configuration.DummyPopulatorConfiguration;
import com.smilan.process.domain.announce.configuration.AnnounceProcessConfiguration;
import com.smilan.process.domain.category.configuration.CategoryProcessConfiguration;
import com.smilan.process.domain.chat.configuration.ChatConfiguration;
import com.smilan.process.domain.media.configuration.MediaProcessConfiguration;
import com.smilan.process.domain.profileUser.configuration.ProfileProcessConfiguration;
import com.smilan.process.domain.security.configuration.SecurityConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Thomas
 *
 */
@Configuration
@Import({
    AnnounceProcessConfiguration.class,
    ProfileProcessConfiguration.class,
    CategoryProcessConfiguration.class,
    MediaProcessConfiguration.class,
    ChatConfiguration.class,
    DummyPopulatorConfiguration.class,
    SecurityConfiguration.class
})
public class ProcessConfiguration {

}
