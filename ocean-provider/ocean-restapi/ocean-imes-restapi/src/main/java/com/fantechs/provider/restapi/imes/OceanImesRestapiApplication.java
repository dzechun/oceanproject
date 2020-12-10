package com.fantechs.provider.restapi.imes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
@ComponentScan({"com.fantechs.provider.restapi.imes","com.fantechs.common"})
@MapperScan({"com.fantechs.provider.restapi.imes.mapper"})
@EnableFeignClients(basePackages = "com.fantechs.provider")
public class OceanImesRestapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanImesRestapiApplication.class, args);
    }

}
