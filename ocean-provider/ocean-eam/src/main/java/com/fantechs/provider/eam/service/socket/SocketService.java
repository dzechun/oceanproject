package com.fantechs.provider.eam.service.socket;

import java.io.IOException;

/**
 * @author Mr.Lei
 * @create 2021/1/20
 */
public interface SocketService {

    int instructions(String ip,String code,Object url);

    void openService() throws IOException;

    /**
     * @create 2021/07/29
     * @throws IOException
     */
    void openServiceEam() throws IOException;

    int BatchInstructions(Long proLine,String code,Object url);
}
