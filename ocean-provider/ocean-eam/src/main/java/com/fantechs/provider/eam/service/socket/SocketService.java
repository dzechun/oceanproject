package com.fantechs.provider.eam.service.socket;

import java.io.IOException;
import java.util.Map;

/**
 * @author Mr.Lei
 * @create 2021/1/20
 */
public interface SocketService {

    int instructions(String ip,String code);

    void manyServer() throws IOException;
}
