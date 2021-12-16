package com.fantechs.provider.restapi.mulinsen;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
@ComponentScan({"com.fantechs.provider.restapi.mulinsen","com.fantechs.common"})
@MapperScan({"com.fantechs.provider.restapi.mulinsen.mapper"})
@EnableFeignClients(basePackages = "com.fantechs.provider")
@EnableDistributedTransaction
public class OceanMulinsenApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanMulinsenApiApplication.class, args);
    }

}
