package com.fantechs.provider.guest.eng.service.impl;

import com.fantechs.common.base.general.dto.restapi.EngReportDeliveryOrderDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.provider.api.guest.fivering.FiveringFeignApi;
import com.fantechs.provider.guest.eng.mapper.EngReportDeliveryOrderOrderMapper;
import com.fantechs.provider.guest.eng.service.EngReportDeliveryOrderOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/09/01.
 */
@Service
public class EngReportDeliveryOrderOrderServiceImpl implements EngReportDeliveryOrderOrderService {

    @Resource
    private EngReportDeliveryOrderOrderMapper engReportDeliveryOrderOrderMapper;
    @Resource
    private FiveringFeignApi fiveringFeignApi;


    @Override
    public String reportDeliveryOrderOrder(WmsOutDeliveryOrder wmsOutDeliveryOrder){
        String jsonVoiceArray="";
        String projectID="3919";
        Map<String, Object> map=new HashMap<>();
        map.put("deliveryOrderId",wmsOutDeliveryOrder.getDeliveryOrderId());
        List<EngReportDeliveryOrderDto> deliveryOrderOrder = engReportDeliveryOrderOrderMapper.findDeliveryOrderOrder(map);


        jsonVoiceArray= JsonUtils.objectToJson(deliveryOrderOrder);
        String s0=jsonVoiceArray.replaceAll("deliveryOrderDetId","WMSKey");
        String s1=s0.replaceAll("option1","ISGUID");
        String s2=s1.replaceAll("option2","IDGUID");
        String s3=s2.replaceAll("option11","实发量");
        String s4=s3.replaceAll("remark","发料备注");
        String s5=s4.replaceAll("createTime","登记时间");
        String s6=s5.replaceAll("createUserName","登记人");

        ResponseEntity<String>  responseEntityResult=fiveringFeignApi.writeIssueDetails (s6,projectID);

        return responseEntityResult.getData();
    }


}
