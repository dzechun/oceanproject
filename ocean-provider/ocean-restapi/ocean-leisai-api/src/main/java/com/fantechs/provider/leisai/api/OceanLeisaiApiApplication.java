package com.fantechs.provider.leisai.api;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,DruidDataSourceAutoConfigure.class})
@EnableDiscoveryClient
@ComponentScan({"com.fantechs.provider.leisai.api","com.fantechs.common"})
@MapperScan({"com.fantechs.provider.leisai.api.mapper"})
@EnableFeignClients(basePackages = "com.fantechs.provider")
public class OceanLeisaiApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanLeisaiApiApplication.class, args);
    }
}
