package com.fantechs.provider.base.util;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.exception.BizErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * @author Mr.Lei
 * @create 2021/1/20
 */
public class SocketClient {
    private static Logger logger = LoggerFactory.getLogger(SocketClient.class);
    //private String ip = "10.182.164.100";
    //private int port = 8098;
    static Socket socket = null;
    public static Boolean isConnect = false;
    public static String massage = null;

    public SocketClient(String ip,Integer port){
        try {
            socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(ip, port);
            socket.connect(socketAddress,5000);
            isConnect = socket.isConnected();
            logger.info("开始连接");
            new ReadSocket().start();
        }catch (Exception e){
            logger.error("连接失败："+e.getMessage());
            throw new BizErrorException("Bartender服务连接失败");
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
            logger.info("发送数据："+out_string);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            logger.error("打印失败："+e.getMessage());
            throw new BizErrorException(ErrorCodeEnum.valueOf("打印数据发送失败！请检查Bartender服务"));
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
