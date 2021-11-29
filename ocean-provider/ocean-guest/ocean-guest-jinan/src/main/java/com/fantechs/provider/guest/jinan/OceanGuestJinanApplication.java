package com.fantechs.provider.guest.jinan;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"com.fantechs.provider.guest.jinan","com.fantechs.common"})
@MapperScan({"com.fantechs.provider.guest.jinan.mapper"})
@EnableFeignClients(basePackages = "com.fantechs.provider")
@EnableDistributedTransaction
public class OceanGuestJinanApplication {
    public static void main(String[] args) {
        SpringApplication.run(OceanGuestJinanApplication.class, args);
    }
}
