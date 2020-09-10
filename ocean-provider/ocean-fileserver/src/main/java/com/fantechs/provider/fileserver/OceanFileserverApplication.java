package com.fantechs.provider.fileserver;

import com.github.tobato.fastdfs.FdfsClientConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.context.annotation.Import;
import org.springframework.jmx.support.RegistrationPolicy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Import(FdfsClientConfig.class)
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class OceanFileserverApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanFileserverApplication.class, args);
    }

    @GetMapping(value = "/index")
    public String index() {
        return "this is user index";
    }

}
