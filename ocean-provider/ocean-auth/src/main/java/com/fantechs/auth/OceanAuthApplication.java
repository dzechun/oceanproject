package com.fantechs.auth;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@EnableDiscoveryClient
@ComponentScan({"com.fantechs.auth","com.fantechs.common"})
@MapperScan({"com.fantechs.auth.mapper"})
@EnableFeignClients(basePackages = "com.fantechs.provider.api")
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
public class OceanAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanAuthApplication.class, args);
    }
}
