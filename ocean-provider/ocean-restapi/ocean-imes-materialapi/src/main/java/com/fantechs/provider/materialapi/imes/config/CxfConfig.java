package com.fantechs.provider.materialapi.imes.config;

import com.fantechs.provider.materialapi.imes.service.MaterialService;

import com.fantechs.provider.materialapi.imes.service.impl.MaterialServiceImpl;
import com.fantechs.provider.materialapi.imes.service.impl.SapWorkOrderServiceImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.Endpoint;

@Configuration
public class CxfConfig {

    @Bean
    public ServletRegistrationBean servletRegistrationBean() {
        return new ServletRegistrationBean(new CXFServlet(),"/material/*");
    }

    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus() {
        return new SpringBus();
    }

    @Bean
    public MaterialService materialService() {
        return new MaterialServiceImpl();
    }

    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(springBus(), materialService());
        endpoint.publish("/api");
        return endpoint;
    }

    @Bean
    public Endpoint another_endpoint() {
            EndpointImpl endpoint = new EndpointImpl(springBus(),  new SapWorkOrderServiceImpl());
            endpoint.publish("/workOrder");   //发布地址
            return endpoint;
    }

}
