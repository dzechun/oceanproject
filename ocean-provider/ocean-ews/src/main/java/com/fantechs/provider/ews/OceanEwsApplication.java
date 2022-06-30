package com.fantechs.provider.ews;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"com.fantechs.provider.ews","com.fantechs.common"})
@EnableDistributedTransaction
public class OceanEwsApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanEwsApplication.class, args);
    }

}
