package com.fantechs.provider.mes.sfc.util;

import com.alibaba.fastjson.JSONObject;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.mes.sfc.PrintDto;
import com.fantechs.provider.mes.sfc.config.RabbitConfig;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.ChannelCallback;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author Mr.Lei
 * @create 2021/2/24
 */
@Component
@Slf4j
public class RabbitProducer {
    @Autowired
    private AmqpTemplate rabbitTemplate;
    @Resource
    private RabbitAdmin rabbitAdmin;

    public void sendDemoQueue(byte[] bytes) throws IOException {
        this.rabbitTemplate.convertAndSend("demoQueue", bytes);
    }

    /**
     * byte 第一位表示文件上传 后16位表示版本号 其次表示文件字节
     * @param version
     * @param multipartFile
     */
    public void sendFiles(String version,String fileName,MultipartFile multipartFile){
        try {
            byte[] filebyte = fileName.getBytes();
            byte[] vs = version.getBytes();
            byte[] file = multipartFile.getBytes();
            byte[] ibytes = new byte[28+file.length];
            ibytes[0]=(byte)2;
            System.arraycopy(filebyte,0,ibytes,1,filebyte.length);
            System.arraycopy(vs,0,ibytes,17,vs.length);
            System.arraycopy(file,0,ibytes,28,file.length);
            this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_NAME_FILE,ibytes);
        }catch (Exception e){
            throw new BizErrorException(e.getMessage());
        }
    }

    /**
     * 第一位1表示打印标签 发送打印字节
     * @param printDto
     */
    public void sendPrint(PrintDto printDto){
        String json = JSONObject.toJSONString(printDto);
        byte[] bytes = json.getBytes();
        byte[] ibytes = new byte[1+bytes.length];
        ibytes[0]=(byte)1;
        System.arraycopy(bytes,0,ibytes,1,bytes.length);
        this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_NAME_FILE,ibytes);
    }

    /**
     * 第一位1表示打印标签 发送打印字节
     * @param id 标识
     * @param printDto
     */
    public void sendPrint(PrintDto printDto,String id){

        try {
            Queue queue = new Queue(RabbitConfig.QUEUE_NAME_FILE+":"+id,true,false,false,null);
            FanoutExchange fanoutExchange = new FanoutExchange(RabbitConfig.DIRECT_EXCHANGE);
            rabbitAdmin.declareQueue(queue);
            rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(fanoutExchange));
        }catch (Exception e){
            log.error("队列已存在或者异常:"+e.getMessage());
        }

        AMQP.Queue.DeclareOk declareOk = rabbitAdmin.getRabbitTemplate().execute(
                new ChannelCallback<AMQP.Queue.DeclareOk>() {
                    @Override
                    public AMQP.Queue.DeclareOk doInRabbit(Channel channel)
                            throws Exception {
                        return channel.queueDeclarePassive(RabbitConfig.QUEUE_NAME_FILE+":"+id);
                    }
                });

        String json = JSONObject.toJSONString(printDto);
        byte[] bytes = json.getBytes();
        byte[] ibytes = new byte[1+bytes.length];
        ibytes[0]=(byte)1;
        System.arraycopy(bytes,0,ibytes,1,bytes.length);
        this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_NAME_FILE+":"+id,ibytes);
    }

    /**
     * 催动PLC堆垛运行
     * @param json
     */
    public void sendStacking(String json){
        String s = 1 + json;
        byte[] bytes = s.getBytes();
        this.rabbitTemplate.convertAndSend(RabbitConfig.STACKING_QUEUE_NAME,bytes);
    }

    /**
     * 发送读头扫码补打信息
     * @param resultJson
     * @param id
     */
    public void sendMakeUp(String resultJson,String id){
        try {
            Queue queue = new Queue(RabbitConfig.QUEUE_NAME_DO +":"+id,true,false,false,null);
            FanoutExchange fanoutExchange = new FanoutExchange(RabbitConfig.DIRECT_EXCHANGE);
            rabbitAdmin.declareQueue(queue);
            rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(fanoutExchange));
        }catch (Exception e){
            log.error("队列已存在或者异常:"+e.getMessage());
        }

        AMQP.Queue.DeclareOk declareOk = rabbitAdmin.getRabbitTemplate().execute(
                new ChannelCallback<AMQP.Queue.DeclareOk>() {
                    @Override
                    public AMQP.Queue.DeclareOk doInRabbit(Channel channel)
                            throws Exception {
                        return channel.queueDeclarePassive(RabbitConfig.QUEUE_NAME_DO+":"+id);
                    }
                });

        String json = JSONObject.toJSONString(resultJson);
        byte[] bytes = json.getBytes();
        byte[] ibytes = new byte[1+bytes.length];
        ibytes[0]=(byte)1;
        System.arraycopy(bytes,0,ibytes,1,bytes.length);
        this.rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_NAME_DO+":"+id,ibytes);
    }

}
