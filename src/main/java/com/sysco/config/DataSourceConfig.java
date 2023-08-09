package com.sysco.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.batch.BatchDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.TransactionManager;

import javax.sql.DataSource;

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(HikariConfigProperties.class)
public class DataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.cds")
    public DataSourceProperties cdsDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public HikariDataSource cdsDataSource(
            @Qualifier("cdsDataSourceProperties") DataSourceProperties cdsDataSourceProperties,
            HikariConfigProperties hikariConfigProperties) {
        HikariDataSource hikariDataSource = cdsDataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
        hikariDataSource.setAutoCommit(hikariConfigProperties.isAutoCommit());
        hikariDataSource.setConnectionTimeout(hikariConfigProperties.getConnectionTimeout());
        hikariDataSource.setValidationTimeout(hikariConfigProperties.getValidationTimeout());
        hikariDataSource.setMaximumPoolSize(hikariConfigProperties.getMaximumPoolSize());
        hikariDataSource.setMinimumIdle(hikariConfigProperties.getMinimumIdle());
        return hikariDataSource;
    }

    @Bean
    public TransactionManager cdsTransactionManager(@Qualifier("cdsDataSource") DataSource cdsDataSource) {
        return new DataSourceTransactionManager(cdsDataSource);
    }

    @Bean
    public NamedParameterJdbcOperations namedParameterJdbcOperations(@Qualifier("cdsDataSource")DataSource cdsDataSource) {
        return new NamedParameterJdbcTemplate(cdsDataSource);
    }


    @Bean
    @BatchDataSource
    public DataSource batchDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("/org/springframework/batch/core/schema-drop-hsqldb.sql")
                .addScript("/org/springframework/batch/core/schema-hsqldb.sql")
                .build();
    }


}
