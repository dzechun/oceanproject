package com.fantechs.provider.client.listener;

import com.fantechs.common.base.electronic.dto.SmtSortingDto;
import com.fantechs.common.base.electronic.entity.SmtClientManage;
import com.fantechs.common.base.electronic.entity.SmtSorting;
import com.fantechs.common.base.electronic.entity.search.SearchSmtSorting;
import com.fantechs.common.base.response.MQResponseEntity;
import com.fantechs.common.base.utils.HTTPUtils;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.RestTemplateUtil;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.electronic.ElectronicTagFeignApi;
import com.fantechs.provider.client.config.RabbitConfig;
import com.fantechs.provider.client.dto.PtlSortingDTO;
import com.fantechs.provider.client.dto.PtlSortingDetailDTO;
import com.fantechs.provider.client.server.impl.FanoutSender;
import lombok.extern.log4j.Log4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by lfz on 2020/11/27.
 */
@Component
public class ElectronicTagReceiver {
    private static final Logger log = LoggerFactory.getLogger(ElectronicTagReceiver.class);
    @Autowired
    private FanoutSender fanoutSender;

    @Autowired
    private ElectronicTagFeignApi electronicTagFeignApi;

    @Value("${mesAPI.resApi}")
    private String resApiUrl;

    // 监听标签队列
    @RabbitListener(queues = RabbitConfig.TOPIC_QUEUE1)
    public void receiveTopic1(byte[]  bytes ) throws UnsupportedEncodingException {
        String encoded= new String(bytes,"UTF-8");
        MQResponseEntity  mqResponseEntity=JsonUtils.jsonToPojo(encoded,MQResponseEntity.class);
        log.info("接收到客户端消息："+mqResponseEntity);
        //电子标签熄灭动作
        if(mqResponseEntity.getCode()==106){
            Map<String, Object> map = JsonUtils.jsonToMap(mqResponseEntity.getData().toString());
            String equipmentId =map.get("GwId").toString();
            String electronicTagId =map.get("TagNode").toString();
            BigInteger b = new BigInteger(electronicTagId).abs();

            //通过标签ID去找当前的分拣单信息
            SearchSmtSorting searchSmtSorting =   new SearchSmtSorting();
            searchSmtSorting.setElectronicTagId(b.toString());
            searchSmtSorting.setEquipmentId(equipmentId);
            searchSmtSorting.setStatus((byte)1);
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
               //分拣单号处理完，回传给MES
               if(StringUtils.isEmpty(findSortingList)){
                   PtlSortingDTO ptlSortingDTO  = new PtlSortingDTO();
                   searchSmtSorting1.setStatus(null);
                   //获取分拣单号的所有物料、储位信息回传MES
                   findSortingList = electronicTagFeignApi.findSortingList(searchSmtSorting1).getData();
                   List<PtlSortingDetailDTO> ptlSortingDetailDTOList = new LinkedList<>();
                   for(SmtSortingDto smtSortingDto1 : findSortingList ){
                       PtlSortingDetailDTO ptlSortingDetailDTO = new PtlSortingDetailDTO();
                       ptlSortingDetailDTO.setCwWarehouseCode(smtSortingDto1.getStorageCode());
                       ptlSortingDetailDTO.setMaterialCode(smtSortingDto1.getMaterialCode());
                       ptlSortingDetailDTOList.add(ptlSortingDetailDTO);
                   }
                   ptlSortingDTO.setSortingCode(smtSortingDto.getSortingCode());
                   ptlSortingDTO.setPtlSortingDetailDTOList(ptlSortingDetailDTOList);
                   log.info("分拣单号处理完，回传给MES："+ptlSortingDTO);
                   String result = RestTemplateUtil.postJsonStrForString(ptlSortingDTO,resApiUrl);
                   log.info("MES返回信息："+result);
//                   mqResponseEntity.setCode(1003);
//            fanoutSender.send(RabbitConfig.FANOUT_QUEUE1, JSONObject.toJSONString(mqResponseEntity));
               }
           }
        }else if(mqResponseEntity.getCode()==102){

        }else if(mqResponseEntity.getCode()==103){

        }
    }

    public static void main(String args[]) throws Exception
    {
        BigInteger b = new BigInteger("-1888832");
        b = b.abs();
        System.out.println(b.toString());
    }
}
