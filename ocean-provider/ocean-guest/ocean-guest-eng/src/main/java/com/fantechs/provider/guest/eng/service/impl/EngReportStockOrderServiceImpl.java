package com.fantechs.provider.guest.eng.service.impl;

import com.fantechs.common.base.general.dto.restapi.EngReportStockOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.provider.api.guest.fivering.FiveringFeignApi;
import com.fantechs.provider.guest.eng.mapper.EngReportStockOrderMapper;
import com.fantechs.provider.guest.eng.service.EngReportStockOrderService;
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
public class EngReportStockOrderServiceImpl  implements EngReportStockOrderService {

    @Resource
    private FiveringFeignApi fiveringFeignApi;
    @Resource
    private EngReportStockOrderMapper engReportStockOrderMapper;


    @Override
    public String reportStockOrder(WmsInnerStockOrder wmsInnerStockOrder ){
        String jsonVoiceArray="";
        String projectID="3919";

        Map<String, Object> map=new HashMap<>();
        map.put("stockOrderId",wmsInnerStockOrder.getStockOrderId());
        List<EngReportStockOrderDto> stockOrders = engReportStockOrderMapper.findStockOrder(map);

        jsonVoiceArray= JsonUtils.objectToJson(stockOrders);
        String s0=jsonVoiceArray.replaceAll("stockOrderDetId","WMSKey");
        String s1=s0.replaceAll("option1","PPGUID");
        String s2=s1.replaceAll("option2","PSGUID");
        String s3=s2.replaceAll("contractCode","合同号");
        String s4=s3.replaceAll("purchaseReqOrderCode","请购单号");
        String s5=s4.replaceAll("materialCode","材料编码");
        String s6=s5.replaceAll("locationNum","位号");
        String s7=s6.replaceAll("dominantTermCode","主项号");
        String s8=s7.replaceAll("deviceCode","装置号");
        String s9=s8.replaceAll("varianceQty","变化量");
        String s10=s9.replaceAll("inventoryStatusName","材料状态");
        //String s11=s10.replaceAll("storageId","DHGUID");
        String s11=s10.replaceAll("createTime","登记时间");
        String s12=s11.replaceAll("createUserName","登记人");

        ResponseEntity<String>  responseEntityResult=fiveringFeignApi.writeMakeInventoryDetails(s12,projectID);
        return responseEntityResult.getData();
    }


}
