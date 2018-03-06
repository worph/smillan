package com.smilan.logic.common.configuration;

import com.smilan.logic.common.configuration.embedded.EmbeddedDatabaseConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@Import({ DatabaseConfiguration.class, EmbeddedDatabaseConfiguration.class, HikariCPConfiguration.class })
public class CommunDatabaseConfiguration {
}