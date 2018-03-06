package com.smilan.logic.common.configuration;

import com.zaxxer.hikari.HikariDataSource;
import java.sql.Driver;
import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.ConnectionProperties;
import org.springframework.jdbc.datasource.embedded.DataSourceFactory;

/**
 * @author Thomas
 *
 */
@Configuration
@Profile({ "default", "HikariCP" })
public class HikariCPConfiguration {

    /**
     * @author Thomas
     *
     */
    private final class HikariCPDataSourceFactory implements DataSourceFactory {

        private final HikariDataSource hikariDataSource = new HikariDataSource();

        public HikariCPDataSourceFactory() {
            this.hikariDataSource.setMaximumPoolSize(Runtime.getRuntime().availableProcessors() * 2 + 2);
        }

        @Override
        public DataSource getDataSource() {
            return this.hikariDataSource;
        }

        @Override
        public ConnectionProperties getConnectionProperties() {
            final HikariCPConnectionProperties hikariCPConnectionProperties = new HikariCPConnectionProperties();
            return hikariCPConnectionProperties;
        }

        /**
         * @author Thomas
         *
         */
        private final class HikariCPConnectionProperties implements ConnectionProperties {

            @Override
            public void setDriverClass(Class<? extends Driver> driverClass) {
                HikariCPDataSourceFactory.this.hikariDataSource.setDriverClassName(driverClass.getName());
            }

            @Override
            public void setUrl(String url) {
                HikariCPDataSourceFactory.this.hikariDataSource.setJdbcUrl(url);
            }

            @Override
            public void setUsername(String username) {
                HikariCPDataSourceFactory.this.hikariDataSource.setUsername(username);
            }

            @Override
            public void setPassword(String password) {
                HikariCPDataSourceFactory.this.hikariDataSource.setPassword(password);
            }
        }
    }

    @Bean
    public DataSourceFactory dataSourceFactory() {
        return new HikariCPDataSourceFactory();
    }

}
