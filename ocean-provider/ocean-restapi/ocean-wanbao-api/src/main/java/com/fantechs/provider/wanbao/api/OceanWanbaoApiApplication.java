package com.fantechs.provider.wanbao.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
@ComponentScan({"com.fantechs.provider.wanbao.api","com.fantechs.common"})
@MapperScan({"com.fantechs.provider.wanbao.api.mapper"})
@EnableFeignClients(basePackages = "com.fantechs.provider")
public class OceanWanbaoApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanWanbaoApiApplication.class, args);
    }
}
