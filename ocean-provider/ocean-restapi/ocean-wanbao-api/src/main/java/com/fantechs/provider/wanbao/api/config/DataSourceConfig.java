package com.fantechs.provider.wanbao.api.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableScheduling
@Slf4j
public class DataSourceConfig {

    @Resource
    private DBProperties dbProperties;

    /**
     * 设置动态数据源，通过@Primary 来确定主DataSource
     *
     */
    @Bean(name = "dataSource")
    public DynamicDataSource dataSource(){
        DynamicDataSource dynamicDataSource = new DynamicDataSource();

        //1.设置默认数据源
        dynamicDataSource.setDefaultTargetDataSource(dbProperties.getPrimary());
        //2.配置多数据源
        Map<Object, Object> map = new HashMap<>();
     //   map.put("primary", dbProperties.getPrimary());
        map.put("secondary", dbProperties.getSecondary());
        map.put("thirdary", dbProperties.getThirdary());
        map.put("fourth", dbProperties.getFourth());
        map.put("five", dbProperties.getFive());

        //3.存放数据源集
        dynamicDataSource.setTargetDataSources(map);
        return dynamicDataSource;
    }
}
