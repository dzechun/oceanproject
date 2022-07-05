package com.xxl.job.executor;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class,DruidDataSourceAutoConfigure.class})
public class OceanXxlJobExecutorApplication {

	public static void main(String[] args) {
        SpringApplication.run(OceanXxlJobExecutorApplication.class, args);
	}

}