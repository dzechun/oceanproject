package com.fantechs.provider.exhibition;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"com.fantechs.provider.exhibition","com.fantechs.common"})
@EnableFeignClients(basePackages = "com.fantechs.provider.api")
public class OceanExhibitionClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(OceanExhibitionClientApplication.class, args);
	}

}
