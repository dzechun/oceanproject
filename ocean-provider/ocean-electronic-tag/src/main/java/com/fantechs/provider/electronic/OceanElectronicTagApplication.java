package com.fantechs.provider.electronic;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@EnableDiscoveryClient
@ComponentScan({"com.fantechs.provider.electronic","com.fantechs.common","com.fantechs.common.base.electronic"})
@MapperScan({"com.fantechs.provider.electronic.mapper"})
@EnableFeignClients(basePackages = "com.fantechs.provider")
public class OceanElectronicTagApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanElectronicTagApplication.class, args);
    }

}
