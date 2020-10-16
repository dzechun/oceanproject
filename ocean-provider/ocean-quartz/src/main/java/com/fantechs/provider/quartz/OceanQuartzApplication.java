package com.fantechs.provider.quartz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"com.fantechs.provider.quartz","com.fantechs.common"})
public class OceanQuartzApplication {

	public static void main(String[] args) {
		SpringApplication.run(OceanQuartzApplication.class, args);
	}

}
