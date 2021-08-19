package com.fantechs.provider.daq.service.socket.impl;

import com.alibaba.fastjson.JSON;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.daq.DaqDataCollect;
import com.fantechs.common.base.general.entity.daq.DaqEquipment;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.daq.mapper.DaqEquipmentMapper;
import com.fantechs.provider.daq.service.DaqDataCollectService;
import com.fantechs.provider.daq.service.socket.SocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * @author Mr.Lei
 * @create 2021/1/20
 */
@Service
public class SocketServiceImpl implements SocketService {
    private static final Logger log = LoggerFactory.getLogger(SocketServiceImpl.class);

    @Resource
    private DaqEquipmentMapper daqEquipmentMapper;

    //新加
    @Resource
    private DaqDataCollectService daqDataCollectService;


    //定义Lock锁对象
    private int port = 8103;
    private int timeOut = 1000 * 30;
    private Map<String, Long> ipMap = new HashMap<>();

    @Scheduled(cron = "0 */1 * * * ?")
    public void checkIpTask() {
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
    public  void openService() throws IOException{
        //创建一个服务端socket
        ServerSocket serverSocket = new ServerSocket(port);

        //调用accept方法等待连接,线程会阻塞状态
        log.info("=============> Socket服务已启动,等待连接");
        Socket socket=null;
        while(true){
            socket =serverSocket.accept();
            new SockerServerThread(socket).start();
        }
    }

    public class SockerServerThread  extends Thread{
        Socket socket=null;

        public SockerServerThread (Socket socket){
            this.socket=socket;
        }
        OutputStream os=null;
        PrintWriter out=null;

        //线程操作响应客户端请求
        public void run(){
            String ip = socket.getInetAddress().getHostAddress();
            try {
                int prot = socket.getPort();
                log.info("有客户端连接，ip" + ip + ",prot" + prot);
                if (socket.getInputStream() != null) {
                    log.info("=============> socket.getPort" + socket.getPort());
                    String jsonStr = inputStreamToString(socket, ip);
                    log.info("=============> json" + jsonStr);
                    if (jsonStr != null) {
                        boolean isJson = isJSON2(jsonStr);
                        if (!isJson){
                            return;
                        }
                        DaqEquipment equipment = getEquipment(ip, null);
                        DaqDataCollect dataCollect = DaqDataCollect.builder()
                                .status((byte) 1)
                                .collectData(jsonStr)
                                .collectTime(new Date())
                                .createTime(new Date())
                                .isDelete((byte) 1)
                                .equipmentId(equipment.getEquipmentId())
                                .build();
                        daqDataCollectService.save(dataCollect);
                        if(equipment.getOnlineStatus() != (byte) 1){
                            updateStatus(ip, (byte) 1);
                        }
                        ipMap.put(ip, System.currentTimeMillis());
                    }
                }
                //读取输入字段，判断是否断开
                while(true) {
                    String jsonStr = inputStreamToString(socket, ip);
                    if( jsonStr!= null ){
                        ipMap.put(ip, System.currentTimeMillis());
                    }
                }

            } catch (Exception e) {
                updateStatus(ip,(byte)0);
                e.printStackTrace();
            }

        }
    }

    // region 私有方法

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

    private  String inputStreamToString(Socket socket ,String ip) throws IOException {
        String str = null;
        try {
            InputStream inputStream = socket.getInputStream();
            byte [] b = new byte[1024];
            int x = inputStream.read(b, 0, b.length);
            str = new String(b,0,x);
        //    log.info("==============心跳包"+str);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            log.info("---断开连接----");
            updateStatus(ip, (byte) 0);
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
            ipMap.remove(ip);
            e.printStackTrace();
        }
        return str;
    }

    public DaqEquipment getEquipment(String ip,String mac){
        Example example = new Example(DaqEquipment.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotEmpty(ip))
            criteria.andEqualTo("equipmentIp",ip);
        if(StringUtils.isNotEmpty(mac))
            criteria.andEqualTo("equipmentMacAddress",mac);

        DaqEquipment daqEquipment = daqEquipmentMapper.selectOneByExample(example);
        if (StringUtils.isEmpty(daqEquipment)){
            throw new BizErrorException("未查询到对应的设备信息");
        }
        example.clear();
        return daqEquipment;
    }

    public int updateStatus(String ip, Byte bytes) {
        DaqEquipment daqEquipment  = getEquipment(ip, null);
        daqEquipment.setOnlineStatus(bytes);
        daqEquipmentMapper.updateByPrimaryKeySelective(daqEquipment);
        return 1;
    }

    // endregion

}
