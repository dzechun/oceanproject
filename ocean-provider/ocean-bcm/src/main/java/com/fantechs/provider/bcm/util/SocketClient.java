package com.fantechs.provider.bcm.util;

import com.alibaba.fastjson.JSON;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mr.Lei
 * @create 2021/1/20
 */
public class SocketClient {
    private static Logger logger = LoggerFactory.getLogger(SocketClient.class);
    private String ip = "10.182.164.100";
    private int port = 8098;
    static Socket socket = null;
    public static Boolean isConnect = false;
    public static String massage = null;

    public SocketClient(){
        try {
            socket = new Socket(ip,port);
            isConnect = socket.isConnected();
            logger.info("开始连接");
            new ReadSocket().start();
        }catch (Exception e){
            logger.error("连接失败："+e.getMessage());
        }
    }

    /**
     * 关闭Socket
     * @return
     */
    public static void closeSocket(){
        try {
            if(socket.isConnected()){
                socket.close();
            }
        }catch (Exception e){
            logger.error(e.getMessage());
        }
    }

    /**
     * 给服务端发消息
     * @param out_string
     */
    public static void out(String out_string) {
        OutputStreamWriter mOutputStreamWriter;
        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(out_string.getBytes("UTF-8"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error("打印失败："+e.getMessage());
            throw new BizErrorException(ErrorCodeEnum.valueOf("打印数据发送失败！请检查服务"));
        }
    }

    /**
     * 获取返回值
     */
    public class ReadSocket extends Thread{
        @Override
        public void run() {
            while (isConnect){
                try {
                    logger.info("读取");
                    // 将服务端接收到的客户端Socket传给socket，用来与该客户端进行通信
                    InputStream inputStream = socket.getInputStream();
                    byte[] bytes = new byte[1024];
                    int len;
                    StringBuilder sb = new StringBuilder();
                    while((len = inputStream.read(bytes)) != -1) {
                        massage = new String(bytes, 0, len,"UTF-8");
                        logger.info(massage);
                        Map<String, Object> map = new HashMap<>();
                        map.put("printName","1234");
                        map.put("tempName","文档2");
                        map.put("no","123456");
                        map.put("size",1);
                        String js = JSON.toJSONString(map);
                        out(js);
                    }
                    inputStream.close();
                }catch (Exception e){
                    logger.error(e.getMessage());
                    break;
                }
            }
        }
    }
}
