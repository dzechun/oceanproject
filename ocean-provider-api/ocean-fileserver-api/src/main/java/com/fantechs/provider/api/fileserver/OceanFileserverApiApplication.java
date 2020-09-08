package com.fantechs.provider.api.fileserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class OceanFileserverApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanFileserverApiApplication.class, args);
    }

}
