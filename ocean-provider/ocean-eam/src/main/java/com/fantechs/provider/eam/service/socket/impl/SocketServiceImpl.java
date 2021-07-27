package com.fantechs.provider.eam.service.socket.impl;

import com.alibaba.fastjson.JSON;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.eam.EamEquipment;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamEquipmentMapper;
import com.fantechs.provider.eam.service.socket.SocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.io.*;
import java.net.InetAddress;
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
    private EamEquipmentMapper eamEquipmentMapper;

    private Hashtable hashtable = new Hashtable();
    private int port = 9302;   //端口
    private int ticket = 30;
    private int timeOut = 1000 * 60;   //超时时间
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
                    log.info("==============检测到设备断开，设备ip："+key);
                    updateStatus(key, (byte) 3);
                    iterator.remove();
                }
            }
        }
    }

    @Override
    public int instructions(String ip,String code,Object url) {
        try {
            Socket socket = (Socket)hashtable.get(ip);
            if(socket == null) throw new BizErrorException("未查询到ip对应的设备信息,请检查设备是否开启");
            OutputStream os = socket.getOutputStream();
            PrintWriter out =new PrintWriter(os);

            Map<String, Object> map = new HashMap();
            Map<String, Object> data = new HashMap();
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            map.put("code", code);
            if(url != null)  map.put("url", url);
            list.add(map);
            data.put("data",list);
            String outMsg = JSON.toJSONString(data);
            out.write(outMsg);
            out.flush();
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }

    }

    @Override
    public  void openService() throws IOException{
        //创建一个服务端socket
        ServerSocket serverSocket=new ServerSocket(port);

        //调用accept方法等待连接,线程会阻塞状态
        log.info("Socket服务已启动,等待连接");
        Socket socket=null;
        while(true){
            socket =serverSocket.accept();
            new SockerServerThread(socket).start();
            log.info("客户端数量"+hashtable.size()+1);

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

            InetAddress addr = socket.getInetAddress();
            String ip = addr.getHostAddress();
            try {

                //开机连接发送新闻命令
                hashtable.put(addr.getHostAddress(),socket);

                Map<String, Object> map = new HashMap();
                Map<String, Object> newMap = new HashMap();
                Map<String, Object> newData = new HashMap();
                List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
                map.put("code", 1201);
                map.put("url", "http://192.168.204.163/#/ESOPDataShow?ip=" + addr.getHostAddress());
                newList.add(map);
                newMap.put("code", 1202);
                newMap.put("url", "http://192.168.204.163/#/YunZhiESOP?ip=" + addr.getHostAddress());
                newList.add(newMap);
                newData.put("data", newList);
                String outMsg = JSON.toJSONString(newData);
                os=socket.getOutputStream();
                out =new PrintWriter(os);
                out.write(outMsg);
                out.flush();

                updateStatus(ip,(byte)1);
                //读取输入字段，判断是否断开
                while(true) {
                    String str = inputStreamToString(socket, addr.getHostAddress());
                    if( str!= null ){
                        ipMap.put(ip, System.currentTimeMillis());
                    }
                    if("1205".equals(str))
                        updateStatus(ip, (byte)2);
                }

            } catch (Exception e) {
                updateStatus(ip,(byte)3);
                e.printStackTrace();
            }

        }
    }

    private  String inputStreamToString(Socket socket ,String ip) throws IOException {
        InputStream inputStream = socket.getInputStream();
        StringBuffer buffer = new StringBuffer();
        InputStreamReader inputStreamReader;
        String str = null;
        try {
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
            hashtable.remove(ip);
            e.printStackTrace();
        }
        return str;
    }



    public EamEquipment getEquipment(String ip){
        Example example = new Example(EamEquipment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("equipmentIp",ip);
        EamEquipment eamEquipment = eamEquipmentMapper.selectOneByExample(example);
        if (StringUtils.isEmpty(eamEquipment)){
            throw new BizErrorException("未查询到ip对应的设备信息");
        }
        example.clear();
        return eamEquipment;
    }


    public int updateStatus(String ip, Byte bytes) {
        EamEquipment eamEquipment = getEquipment(ip);
        eamEquipment.setOnlineStatus(bytes);
        eamEquipmentMapper.updateByPrimaryKeySelective(eamEquipment);
        return 1;
    }

}
