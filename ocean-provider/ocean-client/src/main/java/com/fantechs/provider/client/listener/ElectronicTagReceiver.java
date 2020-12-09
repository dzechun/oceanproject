package com.fantechs.provider.client.listener;

import com.fantechs.common.base.electronic.dto.SmtSortingDto;
import com.fantechs.common.base.electronic.entity.SmtClientManage;
import com.fantechs.common.base.electronic.entity.SmtSorting;
import com.fantechs.common.base.electronic.entity.search.SearchSmtSorting;
import com.fantechs.common.base.response.MQResponseEntity;
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
import java.util.Map;

/**
 * Created by lfz on 2020/11/27.
 */
@Component
public class ElectronicTagReceiver {
    @Autowired
    private FanoutSender fanoutSender;

    @Autowired
    private ElectronicTagFeignApi electronicTagFeignApi;

    // 监听标签队列
    @RabbitListener(queues = RabbitConfig.TOPIC_QUEUE1)
    public void receiveTopic1(byte[]  bytes ) throws UnsupportedEncodingException {
        String encoded= new String(bytes,"UTF-8");
        MQResponseEntity  mqResponseEntity=JsonUtils.jsonToPojo(encoded,MQResponseEntity.class);
        //电子标签熄灭动作
        if(mqResponseEntity.getCode()==106){
            Map<String, Object> map = JsonUtils.jsonToMap(mqResponseEntity.getData().toString());
            String electronicTagId =map.get("GwId").toString();
            //通过标签ID去找当前的分拣单信息
            SearchSmtSorting searchSmtSorting =   new SearchSmtSorting();
            searchSmtSorting.setElectronicTagId(electronicTagId);
           List<SmtSortingDto> findSortingList = electronicTagFeignApi.findSortingList(searchSmtSorting).getData();
           if(StringUtils.isNotEmpty(findSortingList)){
               SmtSortingDto smtSortingDto = findSortingList.get(0);
               SmtSorting smtSorting = new SmtSorting();
               BeanUtils.copyProperties(smtSortingDto,smtSorting);
               smtSorting.setStatus((byte)2);
               smtSorting.setUpdateStatus((byte)0);
               electronicTagFeignApi.updateSmtSorting(smtSorting);

               //熄灭时，根据单号查询是否做完
               SearchSmtSorting searchSmtSorting1 =   new SearchSmtSorting();
               searchSmtSorting1.setSortingCode(smtSortingDto.getSortingCode());
               searchSmtSorting1.setStatus((byte)1);
               findSortingList = electronicTagFeignApi.findSortingList(searchSmtSorting1).getData();
               //分拣单号处理完，回调给MES
               if(StringUtils.isEmpty(findSortingList)){
//                   mqResponseEntity.setCode(1003);
//            fanoutSender.send(RabbitConfig.FANOUT_QUEUE1, JSONObject.toJSONString(mqResponseEntity));
               }
           }
        }else if(mqResponseEntity.getCode()==999){
            SmtClientManage smtClientManage = JsonUtils.jsonToPojo(mqResponseEntity.getData().toString(),SmtClientManage.class);
            smtClientManage.setMonitoringTime(new Date());
            smtClientManage.setLoginTag((byte) 1);
            electronicTagFeignApi.updateClientManage(smtClientManage);

        }else if(mqResponseEntity.getCode()==110){

        }else if(mqResponseEntity.getCode()==112){

        }else if(mqResponseEntity.getCode()==113){

        }
    }
}
