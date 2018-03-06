package com.smilan.logic.common.configuration.embedded;

import com.smilan.api.common.support.PropertyHelper;
import com.smilan.logic.common.configuration.HibernateJPAProviderConfiguration;
import com.smilan.logic.common.configuration.HibernateJPAProviderConfiguration;
import java.util.Properties;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

/**
 * @author Thomas
 *
 */
@Configuration
@Profile({ "Hibernate" })
public class HibernateJPAProviderEmbeddedDatabaseConfiguration extends HibernateJPAProviderConfiguration {

    @Override
    @Bean
    public Properties vendorProperties(Environment environment) {
        Properties vendorProperties = super.vendorProperties(environment);
        PropertyHelper.putIfPresent(vendorProperties, environment, AvailableSettings.HBM2DDL_AUTO);
        PropertyHelper.putIfPresent(vendorProperties, environment, AvailableSettings.HBM2DDL_IMPORT_FILES);
        PropertyHelper.putIfPresent(vendorProperties, environment, AvailableSettings.HBM2DDL_IMPORT_FILES_SQL_EXTRACTOR);
        PropertyHelper.putIfPresent(vendorProperties, environment, AvailableSettings.HBM2DLL_CREATE_NAMESPACES);
        return vendorProperties;
    }

}
