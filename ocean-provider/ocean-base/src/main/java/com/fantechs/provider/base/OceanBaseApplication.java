package com.fantechs.provider.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"com.fantechs.provider.base","com.fantechs.common"})
@MapperScan({"com.fantechs.provider.base.mapper"})
@EnableFeignClients(basePackages = "com.fantechs.provider.api")
public class OceanBaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanBaseApplication.class, args);
    }

}
