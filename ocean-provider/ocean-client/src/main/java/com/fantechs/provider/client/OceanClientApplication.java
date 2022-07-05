package com.fantechs.provider.client;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@EnableDiscoveryClient
@ComponentScan({"com.fantechs.provider.client","com.fantechs.common"})
@MapperScan({"com.fantechs.provider.client.mapper"})
@EnableFeignClients(basePackages = "com.fantechs.provider.api")
public class OceanClientApplication{

    public static void main(String[] args) {
        SpringApplication.run(OceanClientApplication.class, args);
    }
}
