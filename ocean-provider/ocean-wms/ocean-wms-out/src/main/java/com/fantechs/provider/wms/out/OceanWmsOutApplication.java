package com.fantechs.provider.wms.out;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"com.fantechs.provider.wms.out","com.fantechs.common"})
@MapperScan({"com.fantechs.provider.wms.out.mapper"})
@EnableFeignClients(basePackages = "com.fantechs.provider.api")
@EnableDistributedTransaction
public class OceanWmsOutApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanWmsOutApplication.class, args);
    }

}
