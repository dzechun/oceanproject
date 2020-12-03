package com.fantechs.provider.imes.storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"com.fantechs.provider.imes.storage","com.fantechs.common"})
@MapperScan({"com.fantechs.security.mapper"})
@EnableFeignClients(basePackages = "com.fantechs.provider")
public class OceanStorageInventoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(OceanStorageInventoryApplication.class,args);
    }
}
