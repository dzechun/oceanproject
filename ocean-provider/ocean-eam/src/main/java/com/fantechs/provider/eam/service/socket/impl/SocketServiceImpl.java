package com.fantechs.provider.eam.service.socket.impl;

import com.alibaba.fastjson.JSON;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.entity.eam.EamEquipment;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.eam.mapper.EamEquipmentMapper;
import com.fantechs.provider.eam.service.socket.SocketService;
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

    @Resource
    private EamEquipmentMapper eamEquipmentMapper;

    private Hashtable hashtable;



    @Override
    public int instructions(String ip,String code) {
        try {
            Socket socket = (Socket)hashtable.get(ip);
            if(socket == null) throw new BizErrorException("未查询到ip对应的设备信息,请检查设备是否开启");
            OutputStream os = socket.getOutputStream();
            PrintWriter out =new PrintWriter(os);

            Map<String, Object> map = new HashMap();
            Map<String, Object> data = new HashMap();
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            map.put("code", code);
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
    public  void manyServer() throws IOException{
        //1.创建一个服务端socket
        ServerSocket serverSocket=new ServerSocket(9302);
        //2调用accept方法等待连接,线程会阻塞状态
        System.out.println("Socket服务已启动,等待连接");
        System.out.println("接收到客户端连接请求");
        int count=0;
        Socket socket=null;

        while(true){
            count++;
            socket =serverSocket.accept();
            new SockerServerThread(socket).start();
            System.out.println("客户端数量"+count);
        }
    }

    public class SockerServerThread  extends Thread{
        Socket socket=null;


        public SockerServerThread (Socket socket){
            this.socket=socket;
        }
        //获取输入流
        InputStream inputStream= null;
      //  InputStreamReader isr=null;
        //BufferedReader br=null;
        OutputStream os=null;
        PrintWriter out=null;


        //线程操作响应客户端请求
        public void run(){
            InetAddress addr = socket.getInetAddress();
            try {

               /* Socket socketed = hashtable.get(addr.getHostAddress());
                if(socketed != null && socketed != null){
                inputStream = socket.getInputStream();
                InputStreamReader isr=new InputStreamReader(inputStream);
                BufferedReader br=new BufferedReader(isr);
                String  info=null;
                while((info=br.readLine())!=null){//循环读取信息
                    System.out.println("收到客户端的请求:"+info);

                }
                socket.shutdownInput();
                }else{

                }*/

                hashtable = new Hashtable();
                hashtable.put(addr.getHostAddress(),socket);
                os=socket.getOutputStream();
                out =new PrintWriter(os);

                Map<String, Object> map = new HashMap();
                Map<String, Object> data = new HashMap();
                List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                map.put("code", 1201);
               // map.put("url", "http://192.168.204.163/#/YunZhiESOP");
                map.put("url", "https://www.baidu.com/");
                list.add(map);
                Map<String, Object> map2 = new HashMap();
                map2.put("code", 1202);
                map2.put("url", "http://192.168.204.163/#/ESOPDataShow");
                list.add(map2);
                data.put("data",list);
                String outMsg = JSON.toJSONString(data);
                out.write(outMsg);
                out.flush();

                EamEquipment eamEquipment = getEquipment(addr.getHostAddress());
                eamEquipment.setOnlineStatus((byte)1);
                eamEquipmentMapper.updateByPrimaryKey(eamEquipment);


            } catch (Exception e) {
                System.out.println("---CLOSE------" + addr.getHostAddress());
               // hashtable.remove(addr.getHostAddress());
               /* EamEquipment eamEquipment = getEquipment(addr.getHostAddress());
                eamEquipment.setOnlineStatus((byte)0);
                eamEquipmentMapper.insertUseGeneratedKeys(eamEquipment);*/

                e.printStackTrace();
            }

        }
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





}
