package com.fantechs.provider.mes.pm;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"com.fantechs.provider.mes.pm","com.fantechs.common"})
@MapperScan({"com.fantechs.provider.mes.pm.mapper"})
@EnableFeignClients(basePackages = "com.fantechs.provider")
@EnableDistributedTransaction
public class OceanMesPmApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanMesPmApplication.class, args);
    }

}
