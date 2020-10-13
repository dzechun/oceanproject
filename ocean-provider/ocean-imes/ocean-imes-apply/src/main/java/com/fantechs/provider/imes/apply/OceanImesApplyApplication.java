package com.fantechs.provider.imes.apply;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"com.fantechs.provider.imes.apply","com.fantechs.common"})
@MapperScan({"com.fantechs.provider.imes.basic.mapper"})
@EnableFeignClients(basePackages = "com.fantechs.provider")
public class OceanImesApplyApplication {

	public static void main(String[] args) {
		SpringApplication.run(OceanImesApplyApplication.class, args);
	}

}
