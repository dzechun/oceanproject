package com.fantechs.provider.wms.storageBills;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"com.fantechs.provider.wms.storageBills","com.fantechs.common"})
@MapperScan({"com.fantechs.provider.wms.storageBills.mapper","com.fantechs.provider.wms.storageBills.mapper.history"})
@EnableFeignClients(basePackages = "com.fantechs.provider.api")
public class OceanWmsInApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanWmsInApplication.class, args);
    }

}
