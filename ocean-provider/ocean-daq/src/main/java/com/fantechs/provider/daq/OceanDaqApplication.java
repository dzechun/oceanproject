package com.fantechs.provider.daq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"com.fantechs.provider.daq","com.fantechs.common"})
@MapperScan({"com.fantechs.provider.daq.mapper"})
@EnableFeignClients(basePackages = "com.fantechs.provider")
@EnableScheduling
public class OceanDaqApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanDaqApplication.class, args);
    }
}
