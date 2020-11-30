package com.fantechs.provider.client.listener;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.electronic.entity.SmtElectronicTagController;
import com.fantechs.common.base.electronic.entity.search.SearchSmtElectronicTagController;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.MQResponseEntity;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.electronic.ElectronicTagFeignApi;
import com.fantechs.provider.client.config.RabbitConfig;
import com.fantechs.provider.client.server.impl.FanoutSender;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * Created by lfz on 2020/11/27.
 */
@Component
public class ElectronicTagReceiver {
    @Autowired
   private ElectronicTagFeignApi electronicTagFeignApi;
    @Autowired
    private FanoutSender fanoutSender;

    // queues是指要监听的队列的名字
    @RabbitListener(queues = RabbitConfig.TOPIC_QUEUE1)
    public void receiveTopic1(byte[] message) throws UnsupportedEncodingException {

        String messageStr = new String(message, "UTF-8");
        System.out.println("【receiveFanout1监听到消息】" + messageStr);
        MQResponseEntity mqResponseEntity1 =  JsonUtils.jsonToPojo(messageStr,MQResponseEntity.class);
        if(StringUtils.isEmpty(mqResponseEntity1)){
            new BizErrorException(ErrorCodeEnum.GL99990100);
        }
        MQResponseEntity mqResponseEntity=null;
        //Code==1,初始状态
        if(mqResponseEntity1.getCode()==1){
            SearchSmtElectronicTagController searchSmtElectronicTagController = new SearchSmtElectronicTagController();
            searchSmtElectronicTagController.setPageSize(99999);
            ResponseEntity<List<SmtElectronicTagController>> list =electronicTagFeignApi.findList(searchSmtElectronicTagController);
            mqResponseEntity =  new MQResponseEntity  <List<SmtElectronicTagController>>();
            BeanUtils.copyProperties(list,mqResponseEntity);
            mqResponseEntity.setCode(1);
            mqResponseEntity.setSnedTime(new Date());
        }else  if(mqResponseEntity1.getCode()==3){ //接收电子标签按钮返回的信息

        }
        fanoutSender.send(RabbitConfig.TOPIC_EXCHANGE,RabbitConfig.TOPIC_QUEUE1,mqResponseEntity);
    }
}
