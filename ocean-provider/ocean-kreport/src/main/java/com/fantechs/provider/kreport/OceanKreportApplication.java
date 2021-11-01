package com.fantechs.provider.kreport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"com.fantechs.provider.kreport","com.fantechs.common"})
@EnableFeignClients(basePackages = "com.fantechs.provider")
public class OceanKreportApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanKreportApplication.class, args);
    }

}
