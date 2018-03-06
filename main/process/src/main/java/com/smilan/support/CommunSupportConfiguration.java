package com.smilan.support;

import com.smilan.api.common.dto.Contexte;
import com.smilan.support.builder.ContexteServiceBuilder;
import com.smilan.support.property.PropertyConfiguration;
import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author Thomas
 *
 */
@Configuration
@Import({ PropertyConfiguration.class})
public class CommunSupportConfiguration {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public ContexteService contexteService(Contexte contexte, Clock clock) {
        return new ContexteServiceBuilder()
                .withContexte(contexte)
                .withClock(clock)
                .build();
    }
}
