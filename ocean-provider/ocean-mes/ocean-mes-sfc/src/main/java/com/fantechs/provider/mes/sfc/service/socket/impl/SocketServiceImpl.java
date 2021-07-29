package com.fantechs.provider.mes.sfc.service.socket.impl;

import com.alibaba.fastjson.JSON;
import com.fantechs.common.base.general.entity.eam.EamEquipment;
import com.fantechs.common.base.general.entity.eam.EamDataCollect;
import com.fantechs.provider.api.eam.EamFeignApi;
import com.fantechs.provider.mes.sfc.service.socket.SocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
@Component
public class SocketServiceImpl implements SocketService {
    /*private static final Logger log = LoggerFactory.getLogger(SocketServiceImpl.class);

    @Resource
    private EamDataCollectService eamDataCollectService;
    @Resource
    private EamFeignApi eamFeignApi;

    private int port = 8103;
    private int ticket = 30;
    private int timeOut = 1000 * 30;
    private Map<String, Long> ipMap = new HashMap<>();
    private Socket socket;
    //定义Lock锁对象
    Lock lock = new ReentrantLock();


     */

    //@Scheduled(cron = "0 */1 * * * ?")
    /*public void checkIpTask() {
        log.info("======== 定时器执行");
        if (!ipMap.isEmpty()) {
            Set<Map.Entry<String, Long>> entrySet = ipMap.entrySet();
            Iterator<Map.Entry<String, Long>> iterator = entrySet.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Long> entry = iterator.next();
                String key = entry.getKey();
                Long value = entry.getValue();
                if (System.currentTimeMillis() - value > timeOut) {
                    updateStatus(key, (byte) 3);
                    iterator.remove();
                }
            }
        }
    }

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
                    if (ticket > 0) {
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
                                    boolean isJson = isJSON2(jsonStr);
                                    if (!isJson){
                                        continue;
                                    }
                                    EamEquipment equipment = getEquipment(ip);
                                    EamDataCollect dataCollect = EamDataCollect.builder()
                                            .status((byte) 1)
                                            .collectData(jsonStr)
                                            .collectTime(new Date())
                                            .createTime(new Date())
                                            .isDelete((byte) 1)
                                            .equipmentId(equipment.getEquipmentId())
                                            .build();
                                    eamDataCollectService.save(dataCollect);
                                    if(equipment.getOnlineStatus() != (byte) 1){
                                        updateStatus(ip, (byte) 1);
                                    }
                                    ipMap.put(ip, System.currentTimeMillis());
                                }
                            }
                        } catch (IOException e1) {
                            String ip = socket.getInetAddress().getHostAddress();
                            updateStatus(ip, (byte) 3);
                            e1.printStackTrace();
                        }
                    }
                    //释放同步锁
                    lock.unlock();
                }
            }
        }).start();
    }

    private boolean isJSON2(String str) {
        boolean result = false;
        try {
            Object obj= JSON.parse(str);
            result = true;
        } catch (Exception e) {
            result=false;
        }
        return result;
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
            String ip = socket.getInetAddress().getHostAddress();
            updateStatus(ip, (byte) 0);
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
            e.printStackTrace();
        }
        return str;
    }

    public EamEquipment getEquipment(String ip){
        EamEquipment eamEquipment = eamFeignApi.detailByIp(ip).getData();
        return eamEquipment;
    }

    public int updateStatus(String ip, Byte bytes) {
        EamEquipment eamEquipment = getEquipment(ip);
        eamEquipment.setOnlineStatus(bytes);
        eamFeignApi.update(eamEquipment);
        return 1;
    }*/

}
