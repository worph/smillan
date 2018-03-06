package com.smilan.logic.common.configuration;

import com.smilan.api.common.support.PropertyHelper;
import com.smilan.logic.common.configuration.support.JPASettings;
import java.util.Properties;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

/**
 * @author Thomas
 *
 */
@Configuration
@Import({ EntityManagerConfiguration.class })
public class JPAConfiguration {

    @Bean
    public LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean(JpaVendorAdapter jpaVendorAdapter, Properties vendorProperties, Properties jpaProperties,
            DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactorylean = new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactorylean.setJpaVendorAdapter(jpaVendorAdapter);
        Properties properties = new Properties();
        properties.putAll(jpaProperties);
        properties.putAll(vendorProperties);
        localContainerEntityManagerFactorylean.setJpaProperties(properties);
        localContainerEntityManagerFactorylean.setDataSource(dataSource);
        localContainerEntityManagerFactorylean.setPackagesToScan("com.smilan");//TODO do not hardcode this
        return localContainerEntityManagerFactorylean;
    }

    @Bean
    public JpaTransactionManager jpaTransactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public Properties jpaProperties(Environment environment) {
        Properties jpaProperties = new Properties();
        PropertyHelper.putIfPresent(jpaProperties, environment, JPASettings.SCHEMA_GEN_CREATE_SOURCE);
        PropertyHelper.putIfPresent(jpaProperties, environment, JPASettings.SCHEMA_GEN_DROP_SOURCE);
        PropertyHelper.putIfPresent(jpaProperties, environment, JPASettings.SCHEMA_GEN_CREATE_SCRIPT_SOURCE);
        PropertyHelper.putIfPresent(jpaProperties, environment, JPASettings.SCHEMA_GEN_DROP_SCRIPT_SOURCE);
        PropertyHelper.putIfPresent(jpaProperties, environment, JPASettings.SCHEMA_GEN_DATABASE_ACTION);
        PropertyHelper.putIfPresent(jpaProperties, environment, JPASettings.SCHEMA_GEN_CONNECTION);
        PropertyHelper.putIfPresent(jpaProperties, environment, JPASettings.SCHEMA_GEN_DB_NAME);
        PropertyHelper.putIfPresent(jpaProperties, environment, JPASettings.SCHEMA_GEN_DB_MAJOR_VERSION);
        PropertyHelper.putIfPresent(jpaProperties, environment, JPASettings.SCHEMA_GEN_DB_MINOR_VERSION);
        PropertyHelper.putIfPresent(jpaProperties, environment, JPASettings.SCHEMA_GEN_LOAD_SCRIPT_SOURCE);
        return jpaProperties;
    }

}
