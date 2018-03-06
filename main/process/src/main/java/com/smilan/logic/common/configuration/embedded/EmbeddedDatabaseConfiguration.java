package com.smilan.logic.common.configuration.embedded;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

/**
 * @author Thomas
 *
 */
@Configuration
@Profile({ "EmbeddedDatabase" })
@Import({ JPAEmbeddedDatabaseConfiguration.class, HibernateJPAProviderEmbeddedDatabaseConfiguration.class, H2EmbeddedDataBaseConfiguration.class })
public class EmbeddedDatabaseConfiguration {

}
