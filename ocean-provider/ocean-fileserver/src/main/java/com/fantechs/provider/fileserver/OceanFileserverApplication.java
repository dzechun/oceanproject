package com.fantechs.provider.fileserver;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
@SpringBootApplication(exclude = DruidDataSourceAutoConfigure.class)
@EnableDiscoveryClient
@RestController
@ComponentScan({"com.fantechs.provider","com.fantechs.common"})
public class OceanFileserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanFileserverApplication.class, args);
    }

    @GetMapping(value = "/index")
    public String index() {
        return "this is user index";
    }

}
