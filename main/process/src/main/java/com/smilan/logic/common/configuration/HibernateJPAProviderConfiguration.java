package com.smilan.logic.common.configuration;

import com.smilan.api.common.support.PropertyHelper;
import com.smilan.logic.domain.announce.geolocalisation.MySqlDialectExtended;
import java.util.Properties;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

/**
 * @author Thomas
 *
 */
@Configuration
@Profile({ "default", "Hibernate" })
public class HibernateJPAProviderConfiguration {

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean
    public Properties vendorProperties(Environment environment) {
        Properties vendorProperties = new Properties();
        PropertyHelper.putIfPresent(vendorProperties, environment, AvailableSettings.DIALECT);//TODO clean that
        vendorProperties.put(AvailableSettings.DIALECT, MySqlDialectExtended.class.getName());
        vendorProperties.put(AvailableSettings.SHOW_SQL, environment.getProperty(AvailableSettings.SHOW_SQL, Boolean.FALSE.toString()));
        vendorProperties.put(AvailableSettings.FORMAT_SQL, environment.getProperty(AvailableSettings.FORMAT_SQL, Boolean.TRUE.toString()));
        PropertyHelper.putIfPresent(vendorProperties, environment, AvailableSettings.DEFAULT_SCHEMA);
        PropertyHelper.putIfPresent(vendorProperties, environment, AvailableSettings.DEFAULT_CATALOG);
        PropertyHelper.putIfPresent(vendorProperties, environment, AvailableSettings.SESSION_FACTORY_NAME);
        PropertyHelper.putIfPresent(vendorProperties, environment, AvailableSettings.MAX_FETCH_DEPTH);
        PropertyHelper.putIfPresent(vendorProperties, environment, AvailableSettings.DEFAULT_BATCH_FETCH_SIZE);
        PropertyHelper.putIfPresent(vendorProperties, environment, AvailableSettings.DEFAULT_ENTITY_MODE);
        PropertyHelper.putIfPresent(vendorProperties, environment, AvailableSettings.ORDER_UPDATES);
        vendorProperties.put(AvailableSettings.GENERATE_STATISTICS, environment.getProperty(AvailableSettings.GENERATE_STATISTICS, Boolean.FALSE.toString()));
        vendorProperties.put(AvailableSettings.USE_IDENTIFIER_ROLLBACK, environment.getProperty(AvailableSettings.GENERATE_STATISTICS, Boolean.TRUE.toString()));
        vendorProperties.put(AvailableSettings.USE_SQL_COMMENTS, environment.getProperty(AvailableSettings.USE_SQL_COMMENTS, Boolean.FALSE.toString()));
        vendorProperties.put(AvailableSettings.USE_NEW_ID_GENERATOR_MAPPINGS, environment.getProperty(AvailableSettings.USE_NEW_ID_GENERATOR_MAPPINGS, Boolean.TRUE.toString()));
        PropertyHelper.putIfPresent(vendorProperties, environment, AvailableSettings.QUERY_TRANSLATOR);
        vendorProperties.put(AvailableSettings.USE_REFLECTION_OPTIMIZER, environment.getProperty(AvailableSettings.USE_NEW_ID_GENERATOR_MAPPINGS, Boolean.TRUE.toString()));
        PropertyHelper.putIfPresent(vendorProperties, environment, AvailableSettings.BYTECODE_PROVIDER);
        return vendorProperties;
    }

}
