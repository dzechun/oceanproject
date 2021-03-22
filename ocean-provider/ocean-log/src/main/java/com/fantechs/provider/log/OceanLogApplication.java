package com.fantechs.provider.log;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@ComponentScan({"com.fantechs.provider.log","com.fantechs.common"})
@MapperScan({"com.fantechs.provider.log.mapper"})
@EnableFeignClients(basePackages = "com.fantechs.provider.log")
public class OceanLogApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanLogApplication.class, args);
    }

}
