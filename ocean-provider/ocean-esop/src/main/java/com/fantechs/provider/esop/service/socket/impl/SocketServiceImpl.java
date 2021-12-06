package com.fantechs.provider.esop.service.socket.impl;

import com.alibaba.fastjson.JSON;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysSpecItem;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.entity.security.search.SearchSysSpecItem;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.esop.EsopEquipment;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.esop.mapper.EsopEquipmentMapper;
import com.fantechs.provider.esop.service.EsopIssueService;
import com.fantechs.provider.esop.service.socket.SocketService;
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
    private EsopEquipmentMapper esopEquipmentMapper;
    @Resource
    private SecurityFeignApi securityFeignApi;
    @Resource
    private EsopIssueService esopIssueService;


    //定义Lock锁对象

    private Hashtable hashtable = new Hashtable();
    private int port = 9302;   //端口
    private int timeOut = 1000 * 60 * 10;   //超时时间
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
                    updateStatus(key, (byte) 3,null);
                    iterator.remove();

                    //超时后关闭socket
                    Socket socket = (Socket)hashtable.get(key);
                    try {
                        socket.shutdownInput();
                        socket.shutdownOutput();
                        socket.close();
                        hashtable.remove(key);
                        ipMap.remove(key);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public int instructions(String ip,String code,Object url) {
        try {
            Socket socket = (Socket)hashtable.get(ip);
            if(socket == null) throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"未查询到ip对应的设备信息,请检查设备是否开启");
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
    public int BatchInstructions(Long proLineId,String code,Object url,String type) {
        if(StringUtils.isEmpty(url) || StringUtils.isEmpty(code))  throw new BizErrorException(ErrorCodeEnum.GL99990100.getCode(),"code或url不能为空");
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        Example example = new Example(EsopEquipment.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("orgId", user.getOrganizationId());
        criteria.andEqualTo("status", (byte) 1);
        if(StringUtils.isNotEmpty(proLineId ))
            criteria.andEqualTo("proLineId", proLineId);
        List<EsopEquipment> eamEquipments = esopEquipmentMapper.selectByExample(example);
        //查询是否有问题清单
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("IssueSeconds");
        ResponseEntity<List<SysSpecItem>> specItemList = securityFeignApi.findSpecItemList(searchSysSpecItem);
        for (EsopEquipment eamEquipment : eamEquipments) {
            try {
                Socket socket = (Socket)hashtable.get(eamEquipment.getEquipmentIp());
                if(socket == null || (socket != null && (eamEquipment.getOnlineStatus() ==0 || eamEquipment.getOnlineStatus() ==3 )))
                continue;

                OutputStream os = socket.getOutputStream();
                PrintWriter out =new PrintWriter(os);
                String urlHeader = getUrl();
                Map<String, Object> map = new HashMap();
                Map<String, Object> data = new HashMap();
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                Map<String, Object> managementDate = new HashMap();

                //配置项为展示状态且问题清单有数据。则发送信息。
                if(StringUtils.isNotEmpty(specItemList)&& "1".equals(type)) {
                    String[] paraValue =specItemList.getData().get(0).getParaValue().split(",");
                    Map IssueMap = new HashMap();
                    IssueMap.put("equipmentMacAddress",eamEquipment.getEquipmentMacAddress());
                    IssueMap.put("orgId",user.getOrganizationId());
                    List esopIssues = esopIssueService.findList(IssueMap);
                    if("1".equals(paraValue[0]) && StringUtils.isNotEmpty(esopIssues)){
                        managementDate.put("code", 1207);
                        managementDate.put("url", urlHeader + "/#/IssueList?mac=" + eamEquipment.getEquipmentMacAddress());
                        managementDate.put("seconds", paraValue[1]);
                        managementDate.put("isShow", 1);
                        list.add(managementDate);
                    }
                }

                map.put("code", code);
                map.put("url", urlHeader + url + eamEquipment.getEquipmentMacAddress());
                list.add(map);
                data.put("data",list);
                String outMsg = JSON.toJSONString(data);
                out.write(outMsg);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
        return 1;
    }

    @Override
    public void closeThird(){
        Map<Thread, StackTraceElement[]> maps = Thread.getAllStackTraces();
        for(Thread thirds : maps.keySet()){
            if(thirds.getName().length()> 60 && thirds.getName().substring(thirds.getName().length()-21).equals("ocean-esop-9015.mv.db")){
               thirds.interrupt();
            }
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

                //开机获取mac地址，保存ip
                String mac = inputStreamToString(socket, null,addr.getHostAddress());
                updateStatus(null,(byte)1,mac);
                EsopEquipment equipment = null;
                if(StringUtils.isNotEmpty(mac) && mac.length()>5){
                    equipment = getEquipment(null, mac);
                    equipment.setEquipmentIp(ip);
                    esopEquipmentMapper.updateByPrimaryKeySelective(equipment);
                }
                String url = getUrl();
                //开机连接发送新闻命令
                String localHostIp = InetAddress.getLocalHost().getHostAddress();
                hashtable.put(addr.getHostAddress(),socket);
                Map<String, Object> map = new HashMap();
                Map<String, Object> newMap = new HashMap();
                Map<String, Object> newData = new HashMap();
                Map<String, Object> managementDate = new HashMap();
                List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();

                map.put("code", 1201);
                map.put("url", url+"/#/ESOPDataShow?mac=" + mac);
                newList.add(map);

                //配置项为展示状态且问题清单有数据。则发送信息。
                SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
                searchSysSpecItem.setSpecCode("IssueSeconds");
                ResponseEntity<List<SysSpecItem>> specItemList = securityFeignApi.findSpecItemList(searchSysSpecItem);
                if(StringUtils.isNotEmpty(specItemList)) {
                    String[] paraValue =specItemList.getData().get(0).getParaValue().split(",");
                    Map IssueMap = new HashMap();
                    IssueMap.put("equipmentMacAddress",mac);
                    IssueMap.put("orgId",equipment.getOrgId());
                    List list = esopIssueService.findList(IssueMap);
                    if("1".equals(paraValue[0]) && StringUtils.isNotEmpty(list)){
                        managementDate.put("code", 1207);
                        managementDate.put("url", url+"/#/IssueList?mac=" + mac);
                        managementDate.put("seconds", paraValue[1]);
                        managementDate.put("isShow", 1);
                        newList.add(managementDate);
                    }
                }

                newMap.put("code", 1202);
                newMap.put("url", url+"/#/YunZhiESOP?mac=" + mac);
                newList.add(newMap);

                newData.put("data", newList);
                String outMsg = JSON.toJSONString(newData);
                os=socket.getOutputStream();
                out =new PrintWriter(os);
                out.write(outMsg);
                out.flush();

                //读取输入字段，判断是否断开
                while(true) {
                    String str = inputStreamToString(socket, null,mac);
                    if( str!= null ){
                        ipMap.put(ip, System.currentTimeMillis());
                    }
                    //重连更新mac地址
                    if(StringUtils.isNotEmpty(str) && str.length()>5){
                        equipment = getEquipment(null, mac);
                        equipment.setEquipmentIp(addr.getHostAddress());
                        esopEquipmentMapper.updateByPrimaryKeySelective(equipment);
                        updateStatus(null, (byte)2,mac);
                    }
                }

            } catch (Exception e) {
                updateStatus(ip,(byte)0,null);
                e.printStackTrace();
            }

        }
    }

    private  String inputStreamToString(Socket socket ,String ip,String mac) throws IOException {
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
            updateStatus(null, (byte) 0,mac);
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
            hashtable.remove(ip);
            ipMap.remove(ip);
            e.printStackTrace();
        }

        return str;
    }

    public EsopEquipment getEquipment(String ip,String mac){
        Example example = new Example(EsopEquipment.class);
        Example.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotEmpty(ip))
            criteria.andEqualTo("equipmentIp",ip);
        if(StringUtils.isNotEmpty(mac))
            criteria.andEqualTo("equipmentMacAddress",mac);

        List<EsopEquipment> eamEquipments = esopEquipmentMapper.selectByExample(example);
        if (StringUtils.isEmpty(eamEquipments)){
            throw new BizErrorException(ErrorCodeEnum.OPT20012003.getCode(),"未查询到对应的设备信息");
        }
        example.clear();
        return eamEquipments.get(0);
    }

    public int updateStatus(String ip, Byte bytes,String mac) {
        EsopEquipment eamEquipment  = getEquipment(ip, mac);
        eamEquipment.setOnlineStatus(bytes);
        esopEquipmentMapper.updateByPrimaryKeySelective(eamEquipment);
        return 1;
    }

    public String getUrl(){
        SearchSysSpecItem searchSysSpecItem = new SearchSysSpecItem();
        searchSysSpecItem.setSpecCode("EsopUrl");
        ResponseEntity<List<SysSpecItem>> itemList= securityFeignApi.findSpecItemList(searchSysSpecItem);
        List<SysSpecItem> sysSpecItemList = itemList.getData();
        if(StringUtils.isNotEmpty(sysSpecItemList)){
            return sysSpecItemList.get(0).getParaValue();
        }
        return null;
    }
}
