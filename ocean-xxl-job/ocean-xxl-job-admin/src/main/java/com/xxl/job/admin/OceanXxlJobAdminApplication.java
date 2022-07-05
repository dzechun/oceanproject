package com.xxl.job.admin;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude = {
		DataSourceTransactionManagerAutoConfiguration.class,
		DruidDataSourceAutoConfigure.class,
		HibernateJpaAutoConfiguration.class})
public class OceanXxlJobAdminApplication {

	public static void main(String[] args) {
        SpringApplication.run(OceanXxlJobAdminApplication.class, args);
	}

}