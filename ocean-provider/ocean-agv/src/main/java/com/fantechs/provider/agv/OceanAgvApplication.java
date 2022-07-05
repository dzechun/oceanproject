package com.fantechs.provider.agv;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@EnableDiscoveryClient
@ComponentScan({"com.fantechs.provider.agv","com.fantechs.common"})
@EnableFeignClients(basePackages = "com.fantechs.provider")
public class OceanAgvApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanAgvApplication.class, args);
    }

}
