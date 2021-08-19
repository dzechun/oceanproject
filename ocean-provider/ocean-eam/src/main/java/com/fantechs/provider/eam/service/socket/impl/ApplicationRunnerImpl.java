package com.fantechs.provider.eam.service.socket.impl;

import com.fantechs.provider.eam.service.socket.SocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ApplicationRunnerImpl implements ApplicationRunner {
    private static final Logger log = LoggerFactory.getLogger(ApplicationRunnerImpl.class);

    @Resource
    private SocketService socketService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("通过实现ApplicationRunner接口，在spring boot项目esop启动后启动socket服务端");
        socketService.openService();
    }
}
