package com.fantechs.provider.bcm.service.socket.impl;

import com.alibaba.fastjson.JSON;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.provider.bcm.service.socket.SocketService;
import com.fantechs.provider.bcm.util.SocketClient;

import org.springframework.stereotype.Service;

import java.net.Socket;
import java.util.Map;

/**
 * @author Mr.Lei
 * @create 2021/1/20
 */
@Service
public class SocketServiceImpl implements SocketService {
    @Override
    public String sender(Map<String, Object> map) {
        String outMsg = JSON.toJSONString(map);
        SocketClient.out(outMsg);
        return SocketClient.massage;
    }

    @Override
    public int start(String ip, Integer port) {
        new SocketClient(ip,port);
        return 1;
    }

    @Override
    public int stop() {
        try {
            if(SocketClient.isConnect){
                SocketClient.closeSocket();
            }
        }catch (Exception e){
            throw new BizErrorException(ErrorCodeEnum.valueOf(e.getMessage()));
        }
        return 1;
    }
}
