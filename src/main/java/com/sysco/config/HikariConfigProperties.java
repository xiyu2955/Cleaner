package com.sysco.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spring.datasource.hikari")
public class HikariConfigProperties {

    private boolean autoCommit=false;
    private int connectionTimeout=3000;
    private int validationTimeout=3000;
    private int maxLifetime=60000;
    private int maximumPoolSize=20;
    private int minimumIdle=1;
}
