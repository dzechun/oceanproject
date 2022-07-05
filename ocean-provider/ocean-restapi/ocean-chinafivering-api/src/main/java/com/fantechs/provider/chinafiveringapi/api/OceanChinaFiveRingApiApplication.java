package com.fantechs.provider.chinafiveringapi.api;


import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@EnableDiscoveryClient
@ComponentScan({"com.fantechs.provider.chinafiveringapi.api","com.fantechs.common"})
@MapperScan({"com.fantechs.provider.chinafiveringapi.api.mapper"})
@EnableFeignClients(basePackages = "com.fantechs.provider")
public class OceanChinaFiveRingApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(OceanChinaFiveRingApiApplication.class, args);
    }
}
