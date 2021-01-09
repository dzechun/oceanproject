package com.fantechs.provider.qms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"com.fantechs.provider.qms","com.fantechs.common"})
@MapperScan({"com.fantechs.provider.qms.mapper"})
@EnableFeignClients(basePackages = "com.fantechs.provider.api")
public class OceanQmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanQmsApplication.class, args);
    }

}
