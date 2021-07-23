package com.fantechs.provider.eam.service.socket.impl;

import com.alibaba.fastjson.JSON;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.eam.EamEquipment;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamEquipmentMapper;
import com.fantechs.provider.eam.service.socket.SocketService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.PostConstruct;
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

    @Resource
    private EamEquipmentMapper eamEquipmentMapper;

    private Hashtable hashtable = new Hashtable();



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
    //@PostConstruct
    public  void openService() throws IOException{
        //创建一个服务端socket
        ServerSocket serverSocket=new ServerSocket(9302);

        //调用accept方法等待连接,线程会阻塞状态
        System.out.println("Socket服务已启动,等待连接");
        System.out.println("接收到客户端连接请求");
        Socket socket=null;
        while(true){
            socket =serverSocket.accept();
            new SockerServerThread(socket).start();
            System.out.println("客户端数量"+hashtable.size()+1);

        }
    }

    public class SockerServerThread  extends Thread{
        Socket socket=null;

        public SockerServerThread (Socket socket){
            this.socket=socket;
        }
        // 获取输入流
        // InputStream inputStream= null;
        OutputStream os=null;
        PrintWriter out=null;

        //线程操作响应客户端请求
        public void run(){
            InetAddress addr = socket.getInetAddress();
            String ip = addr.getHostAddress();
            try {
             //   os=socket.getOutputStream();
             //   out =new PrintWriter(os);
                //开机连接发送新闻命令
                hashtable.put(addr.getHostAddress(),socket);

                System.out.println("-----ip----"+ip);

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
                //平板发送数据测试,循环一周时间
                for(int i=0;i<1000000;i++) {
                    sleep(5000);
                    os=socket.getOutputStream();
                    out =new PrintWriter(os);
                    out.write(outMsg);
                    out.flush();
                    updateStatus(ip, (byte) 1);
               }

                //读取输入字段，判断是否断开
                inputStreamToString(socket, addr.getHostAddress());

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
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            byte [] b = new byte[1024];
            int x = inputStream.read(b, 0, b.length);
            str = new String(b,0,x);

            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("---断开连接----");
            updateStatus(ip, (byte) 0);
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();
            hashtable.remove(ip);
            e.printStackTrace();
            return null;
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
