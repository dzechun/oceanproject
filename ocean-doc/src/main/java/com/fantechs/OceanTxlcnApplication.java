package com.fantechs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class OceanTxlcnApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanTxlcnApplication.class, args);
    }

}
