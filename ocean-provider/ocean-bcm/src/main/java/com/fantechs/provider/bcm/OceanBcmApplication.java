package com.fantechs.provider.bcm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"com.fantechs.provider.bcm","com.fantechs.common"})
@MapperScan({"com.fantechs.provider.client.mapper"})
@EnableFeignClients(basePackages = "com.fantechs.provider.api")
public class OceanBcmApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanBcmApplication.class, args);
    }

}
