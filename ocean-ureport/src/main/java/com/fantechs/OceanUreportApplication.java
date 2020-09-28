package com.fantechs;

import com.bstek.ureport.console.UReportServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:ureport-console-context.xml") // 加载ureport对应的xml配置文件
public class OceanUreportApplication {

    public static void main(String[] args) {
        SpringApplication.run(OceanUreportApplication.class, args);
    }

        @Bean //定义ureport的启动servlet
        @SuppressWarnings("unchecked")
        public ServletRegistrationBean ureportServlet() {
            ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new UReportServlet());
            servletRegistrationBean.addUrlMappings("/ureport/*");
            return servletRegistrationBean;
        }
    }
