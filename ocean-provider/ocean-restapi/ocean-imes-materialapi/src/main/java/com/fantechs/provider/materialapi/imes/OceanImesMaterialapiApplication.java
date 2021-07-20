package com.fantechs.provider.materialapi.imes;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class})
@EnableDiscoveryClient
@ComponentScan({"com.fantechs.provider.materialapi.imes","com.fantechs.common"})
@MapperScan({"com.fantechs.provider.materialapi.imes.mapper"})
@EnableFeignClients(basePackages = "com.fantechs.provider")
public class OceanImesMaterialapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanImesMaterialapiApplication.class, args);
    }
}
