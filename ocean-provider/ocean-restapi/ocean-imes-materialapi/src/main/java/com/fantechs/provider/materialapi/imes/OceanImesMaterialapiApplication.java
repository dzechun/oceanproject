package com.fantechs.provider.materialapi.imes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
@ComponentScan({"com.fantechs.provider.materialapi.imes","com.fantechs.common"})
@MapperScan({"com.fantechs.provider.materialapi.imes.mapper"})
@EnableFeignClients(basePackages = "com.fantechs.provider")
public class OceanImesMaterialapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanImesMaterialapiApplication.class, args);
    }
}