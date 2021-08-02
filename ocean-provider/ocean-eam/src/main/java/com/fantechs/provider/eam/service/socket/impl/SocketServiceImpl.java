package com.fantechs.provider.eam.service.socket.impl;

import com.alibaba.fastjson.JSON;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.eam.EamDataCollect;
import com.fantechs.common.base.general.entity.eam.EamEquipment;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.eam.mapper.EamEquipmentMapper;
import com.fantechs.provider.eam.service.EamDataCollectService;
import com.fantechs.provider.eam.service.EamIssueService;
import com.fantechs.provider.eam.service.socket.SocketService;
import com.google.gson.Gson;
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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Mr.Lei
 * @create 2021/1/20
 */
@Service
public class SocketServiceImpl implements SocketService {
    private static final Logger log = LoggerFactory.getLogger(SocketServiceImpl.class);

    @Resource
    private EamEquipmentMapper eamEquipmentMapper;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private EamIssueService eamIssueService;

    //新加*************
    @Resource
    private EamDataCollectService eamDataCollectService;


    private Socket socket;
    //定义Lock锁对象
    Lock lock = new ReentrantLock();
    private int portEam = 8103;
    private int ticketEam = 30;
    private int timeOutEam = 1000 * 30;
    private Map<String, Long> ipMapEam = new HashMap<>();
    //新加*************

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

    @Scheduled(cron = "0 */1 * * * ?")
    public void checkIpTaskEam() {
        log.info("======== 定时器执行");
        if (!ipMapEam.isEmpty()) {
            Set<Map.Entry<String, Long>> entrySet = ipMapEam.entrySet();
            Iterator<Map.Entry<String, Long>> iterator = entrySet.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Long> entry = iterator.next();
                String key = entry.getKey();
                Long value = entry.getValue();
                if (System.currentTimeMillis() - value > timeOutEam) {
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
            if(StringUtils.isNotEmpty(url))
                map.put("url", url);
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

    @Override
    public void openServiceEam() throws IOException {
        //创建一个服务端socket
        ServerSocket serverSocket = new ServerSocket(portEam);

        //调用accept方法等待连接,线程会阻塞状态
        log.info("=============> Socket服务已启动,等待连接");
        new Thread(new Runnable() {
            public void run() {
                //调用服务器套接字对象中的方法accept()获取客户端套接字对象

                while (true) {
                    //添加同步锁
                    lock.lock();
                    if (ticketEam > 0) {
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
                                    EamEquipment equipment = getEquipment(ip, null);
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
                                    ipMapEam.put(ip, System.currentTimeMillis());
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

                //开机获取mac地址，保存ip
                String mac = inputStreamToString(socket, addr.getHostAddress());
                log.info("----mac地址----"+mac);
                if(StringUtils.isNotEmpty(mac) && mac.length()>5){
                    EamEquipment equipment = getEquipment(null, mac);
                    equipment.setEquipmentIp(addr.getHostAddress());
                    eamEquipmentMapper.updateByPrimaryKeySelective(equipment);
                }

                //开机连接发送新闻命令
                hashtable.put(addr.getHostAddress(),socket);
                Map<String, Object> map = new HashMap();
                Map<String, Object> newMap = new HashMap();
                Map<String, Object> newData = new HashMap();
                Map<String, Object> managementDate = new HashMap();
                List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
                map.put("code", 1201);
                map.put("url", "http://192.168.204.163/#/ESOPDataShow?ip=" + addr.getHostAddress());
                newList.add(map);
                newMap.put("code", 1202);
                newMap.put("url", "http://192.168.204.163/#/YunZhiESOP?ip=" + addr.getHostAddress());
                newList.add(newMap);

                //配置项为展示状态且问题清单有数据。则发送信息。
                SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
                searchSysSpecItem.setSpecCode("IssueSeconds");
                List<SysSpecItem> specItemList = securityFeignApi.findSpecItemList(searchSysSpecItem).getData();
                if(StringUtils.isNotEmpty(specItemList)) {
                    String[] paraValue =specItemList.get(0).getParaValue().split(",");
                    Map IssueMap = null;
                    IssueMap.put("equipmentIp",addr.getHostAddress());
                    List list = eamIssueService.findList(IssueMap);
                    if("1".equals(paraValue[0]) && StringUtils.isNotEmpty(list)){
                        managementDate.put("code", 1207);
                        managementDate.put("url", "http://192.168.204.163/#/IssueList?ip=" + addr.getHostAddress());
                        managementDate.put("seconds", paraValue[1]);
                        managementDate.put("isShow", 1);
                        newList.add(managementDate);
                    }
                }
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
                    //重连更新mac地址
                    if(StringUtils.isNotEmpty(str) && str.length()>5){
                        EamEquipment equipment = getEquipment(null, mac);
                        equipment.setEquipmentIp(addr.getHostAddress());
                        eamEquipmentMapper.updateByPrimaryKeySelective(equipment);
                        updateStatus(ip, (byte)2);
                    }
                }

            } catch (Exception e) {
                updateStatus(ip,(byte)0);
                e.printStackTrace();
            }

        }
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
            hashtable.remove(ip);
            ipMap.remove(ip);
            e.printStackTrace();
        }
        return str;
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

    public EamEquipment getEquipment(String ip,String mac){
        Example example = new Example(EamEquipment.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotEmpty(ip))
            criteria.andEqualTo("equipmentIp",ip);
        if(StringUtils.isNotEmpty(mac))
            criteria.andEqualTo("equipmentMacAddress",mac);

        EamEquipment eamEquipment = eamEquipmentMapper.selectOneByExample(example);
        if (StringUtils.isEmpty(eamEquipment)){
            throw new BizErrorException("未查询到对应的设备信息");
        }
        example.clear();
        return eamEquipment;
    }


    public int updateStatus(String ip, Byte bytes) {
        EamEquipment eamEquipment  = getEquipment(ip, null);
        eamEquipment.setOnlineStatus(bytes);
        eamEquipmentMapper.updateByPrimaryKeySelective(eamEquipment);

        return 1;
    }

    @Override
    public int BatchInstructions(Long proLineId,String code,Object url) {
        if(StringUtils.isEmpty(url) || StringUtils.isEmpty(code))  throw new BizErrorException("code或url不能为空");
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        Example example = new Example(EamEquipment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", user.getOrganizationId());
       // criteria.andEqualTo("usageStatus", (byte) 1);
        criteria.andEqualTo("status", (byte) 1);
        if(StringUtils.isNotEmpty(proLineId ))
            criteria.andEqualTo("proLineId", proLineId);
        List<EamEquipment> eamEquipments = eamEquipmentMapper.selectByExample(example);

        for (EamEquipment eamEquipment : eamEquipments) {
            try {
                Socket socket = (Socket)hashtable.get(eamEquipment.getEquipmentIp());
                if(socket == null)  continue;
                    //throw new BizErrorException("未查询到ip对应的设备信息,请检查设备是否开启");
                OutputStream os = socket.getOutputStream();
                PrintWriter out =new PrintWriter(os);

                Map<String, Object> map = new HashMap();
                Map<String, Object> data = new HashMap();
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                map.put("code", code);
                map.put("url", url+eamEquipment.getEquipmentIp());
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
        return 1;
    }

}
