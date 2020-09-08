package com.fantechs;

import com.codingapi.txlcn.tm.config.EnableTransactionManagerServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableTransactionManagerServer
public class OceanTxlcnApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanTxlcnApplication.class, args);
    }

}
