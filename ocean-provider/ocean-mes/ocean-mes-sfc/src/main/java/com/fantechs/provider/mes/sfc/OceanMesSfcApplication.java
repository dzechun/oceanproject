package com.fantechs.provider.mes.sfc;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"com.fantechs.provider.mes.sfc","com.fantechs.common"})
@MapperScan({"com.fantechs.provider.mes.sfc.mapper"})
@EnableFeignClients(basePackages = "com.fantechs.provider.api")
@EnableDistributedTransaction
public class OceanMesSfcApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanMesSfcApplication.class, args);
    }

}
