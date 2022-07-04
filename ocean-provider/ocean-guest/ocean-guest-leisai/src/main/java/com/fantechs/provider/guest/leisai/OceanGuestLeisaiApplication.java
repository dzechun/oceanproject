package com.fantechs.provider.guest.leisai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"com.fantechs.provider.guest.leisai","com.fantechs.common"})
@MapperScan({"com.fantechs.provider.guest.leisai.mapper"})
@EnableFeignClients(basePackages = "com.fantechs.provider")
public class OceanGuestLeisaiApplication {
    public static void main(String[] args) {
        SpringApplication.run(OceanGuestLeisaiApplication.class, args);
    }
}
