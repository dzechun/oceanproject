package com.fantechs.provider.mes.sfc.service.socket.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.general.dto.eam.EamEquipmentDto;
import com.fantechs.common.base.general.entity.eam.search.SearchEamEquipment;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcDataCollect;
import com.fantechs.provider.api.eam.EamFeignApi;
import com.fantechs.provider.mes.sfc.service.MesSfcDataCollectService;
import com.fantechs.provider.mes.sfc.service.socket.SocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class SocketServiceImpl implements SocketService {
    private static final Logger log = LoggerFactory.getLogger(SocketServiceImpl.class);

    @Resource
    private MesSfcDataCollectService mesSfcDataCollectService;
    @Resource
    private EamFeignApi eamFeignApi;

    private int port = 8103;

    private int ticket = 30;
    private Socket socket;
    //定义Lock锁对象
    Lock lock = new ReentrantLock();

//    @PostConstruct
    @Override
    public void openService() throws IOException {
        //创建一个服务端socket
        ServerSocket serverSocket = new ServerSocket(port);

        //调用accept方法等待连接,线程会阻塞状态
        log.info("=============> Socket服务已启动,等待连接");
        new Thread(new Runnable() {
            public void run() {
                //调用服务器套接字对象中的方法accept()获取客户端套接字对象
                while (true) {
                    //添加同步锁
                    lock.lock();
                    if(ticket > 0){
                        try {
                            //等待服务器链接 accept是阻塞的，作为服务器，需要一直等待客户端链接
                            socket = serverSocket.accept();
                            String ip = socket.getInetAddress().getHostAddress();
                            int prot = socket.getPort();
                            log.info("有客户端连接，ip" + ip + ",prot" + prot);
                            if (socket.getInputStream() != null) {
                                log.info("=============> socket.getPort" + socket.getPort());
                                String jsonStr = inputStreamToString(socket);
                                log.info("=============> json" + jsonStr);
                                if (jsonStr != null) {
                                    SearchEamEquipment searchEamEquipment = new SearchEamEquipment();
                                    searchEamEquipment.setEquipmentIp(ip);
                                    List<EamEquipmentDto> equipmentDtos = eamFeignApi.findList(searchEamEquipment).getData();
                                    MesSfcDataCollect dataCollect = MesSfcDataCollect.builder()
                                            .status((byte) 1)
                                            .collectData(jsonStr)
                                            .collectTime(new Date())
                                            .createTime(new Date())
                                            .isDelete((byte) 1)
                                            .build();
                                    if(!equipmentDtos.isEmpty()){
                                        dataCollect.setEquipmentId(equipmentDtos.get(0).getEquipmentId());
                                    }
                                    mesSfcDataCollectService.save(dataCollect);
                                }
                            }
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                    //释放同步锁
                    lock.unlock();
                }
            }
        }).start();
    }

    private String inputStreamToString(Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        InputStreamReader inputStreamReader;
        String str = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            byte[] b = new byte[1024];
            int x = inputStream.read(b, 0, b.length);
            str = new String(b, 0, x);

            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            log.info("=============> 断开连接");
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
            e.printStackTrace();
        }
        return str;
    }

}
