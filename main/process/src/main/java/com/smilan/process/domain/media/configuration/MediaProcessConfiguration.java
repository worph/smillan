package com.smilan.process.domain.media.configuration;

import com.smilan.api.domain.media.MediaManager;
import com.smilan.logic.domain.media.MediaLogic;
import com.smilan.logic.domain.media.configuration.MediaLogicConfiguration;
import com.smilan.process.domain.media.builder.DefaultMediaManagerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Thomas
 *
 */
@Configuration
@Import(MediaLogicConfiguration.class)
public class MediaProcessConfiguration {

    @Bean
    public MediaManager mediaManager(MediaLogic personneLogic) {
        return new DefaultMediaManagerBuilder().withMediaLogic(personneLogic).build();
    }
    
}
