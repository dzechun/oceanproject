package com.fantechs.provider.bcm.service.socket;

import java.util.Map;

/**
 * @author Mr.Lei
 * @create 2021/1/20
 */
public interface SocketService {
    String sender(Map<String, Object>map);

    int start(String ip,Integer port);

    int stop();
}
