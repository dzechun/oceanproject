package com.fantechs.provider.tem;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@EnableDiscoveryClient
@ComponentScan({"com.fantechs.provider.tem","com.fantechs.common"})
@MapperScan({"com.fantechs.provider.tem.mapper"})
@EnableFeignClients(basePackages = "com.fantechs.provider")
public class OceanTemApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanTemApplication.class, args);
    }

}
