package com.fantechs.provider.eam;

import com.fantechs.provider.eam.service.socket.SocketService;
import com.fantechs.provider.eam.service.socket.impl.SocketServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

import java.io.IOException;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan({"com.fantechs.provider.eam","com.fantechs.common"})
@MapperScan({"com.fantechs.provider.eam.mapper"})
@EnableFeignClients(basePackages = "com.fantechs.provider")
public class OceanEamApplication {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(OceanEamApplication.class, args);
        /*SocketServiceImpl socketServiceImpl = new SocketServiceImpl();
        socketServiceImpl.openService();*/
    }

}
