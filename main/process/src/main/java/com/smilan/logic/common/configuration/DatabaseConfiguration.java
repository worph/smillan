package com.smilan.logic.common.configuration;

import com.smilan.api.common.support.PropertyHelper;
import com.smilan.logic.common.configuration.support.JPASettings;
import java.sql.Driver;
import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.embedded.ConnectionProperties;
import org.springframework.jdbc.datasource.embedded.DataSourceFactory;

/**
 * @author Thomas
 *
 */
@Profile({"default", "!EmbeddedDatabase" })
@Import({ JPAConfiguration.class, HibernateJPAProviderConfiguration.class })
public class DatabaseConfiguration {
    
    @Bean
    public Properties dataBaseProperties(Environment environment) {
        Properties jpaProperties = new Properties();
        PropertyHelper.putIfPresent(jpaProperties, environment, JPASettings.JDBC_DRIVER);
        PropertyHelper.putIfPresent(jpaProperties, environment, JPASettings.JDBC_URL);
        PropertyHelper.putIfPresent(jpaProperties, environment, JPASettings.JDBC_USER);
        PropertyHelper.putIfPresent(jpaProperties, environment, JPASettings.JDBC_PASSWORD);
        return jpaProperties;
    }
    
    @Bean
    public DataSource dataSource(DataSourceFactory dataSourceFactory,Properties dataBaseProperties) throws ClassNotFoundException {
        final ConnectionProperties connectionProperties = dataSourceFactory.getConnectionProperties();
        final Class<? extends Driver> driverClass = (Class<? extends Driver>) Class.forName(dataBaseProperties.getProperty(JPASettings.JDBC_DRIVER));
        connectionProperties.setDriverClass(driverClass);
        connectionProperties.setUrl(dataBaseProperties.getProperty(JPASettings.JDBC_URL));
        connectionProperties.setUsername(dataBaseProperties.getProperty(JPASettings.JDBC_USER));
        connectionProperties.setPassword(dataBaseProperties.getProperty(JPASettings.JDBC_PASSWORD));
        return dataSourceFactory.getDataSource();
    }
}
