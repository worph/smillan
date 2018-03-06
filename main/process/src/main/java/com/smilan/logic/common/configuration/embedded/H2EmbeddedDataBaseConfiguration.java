package com.smilan.logic.common.configuration.embedded;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.embedded.DataSourceFactory;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.vendor.Database;

/**
 * @author Thomas
 *
 */
@Configuration
@Profile({"EmbeddedH2" })
public class H2EmbeddedDataBaseConfiguration {

    @Bean
    public Database database() {
        return Database.H2;
    }

    @Bean
    public DataSource dataSource(DataSourceFactory dataSourceFactory) {
        EmbeddedDatabaseBuilder embeddedDatabaseBuilder = new EmbeddedDatabaseBuilder().generateUniqueName(true).setType(EmbeddedDatabaseType.H2).ignoreFailedDrops(true);
        if (dataSourceFactory != null) {
            embeddedDatabaseBuilder.setDataSourceFactory(dataSourceFactory);
        }
        return embeddedDatabaseBuilder.build();
    }

}
