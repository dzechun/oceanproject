package com.fantechs.provider.wms.out;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@EnableDiscoveryClient
@ComponentScan({"com.fantechs.provider.wms.out","com.fantechs.common"})
@MapperScan({"com.fantechs.provider.wms.out.mapper"})
@EnableFeignClients(basePackages = "com.fantechs.provider.api")
public class OceanWmsOutApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanWmsOutApplication.class, args);
    }

}
