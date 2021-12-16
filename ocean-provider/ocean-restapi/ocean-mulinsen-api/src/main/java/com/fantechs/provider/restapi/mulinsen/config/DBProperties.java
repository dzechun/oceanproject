package com.fantechs.provider.restapi.mulinsen.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Component
@Data
@ConfigurationProperties(prefix = "spring.datasource")
public class DBProperties {
    private HikariDataSource primary;
}
