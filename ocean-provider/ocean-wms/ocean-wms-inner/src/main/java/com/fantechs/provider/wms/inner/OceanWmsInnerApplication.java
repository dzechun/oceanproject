package com.fantechs.provider.wms.inner;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"com.fantechs.provider.wms.inner","com.fantechs.common.base.general.entity.wms.inner","com.fantechs.common"})
@MapperScan({"com.fantechs.provider.wms.inner.mapper"})
@EnableFeignClients(basePackages = "com.fantechs.provider")
@EnableDistributedTransaction
public class OceanWmsInnerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanWmsInnerApplication.class, args);
    }

}
