package com.fantechs.provider.guest.wanbao;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"com.fantechs.provider.guest.wanbao","com.fantechs.common"})
@MapperScan({"com.fantechs.provider.guest.wanbao.mapper"})
@EnableFeignClients(basePackages = "com.fantechs.provider")
@EnableDistributedTransaction
public class OceanGuestWanbaoApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanGuestWanbaoApplication.class, args);
    }
}
