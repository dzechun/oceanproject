package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.general.dto.restapi.WmsDataExportInnerJobOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.support.BaseService;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.provider.api.guest.fivering.FiveringFeignApi;
import com.fantechs.provider.wms.inner.mapper.WmsDataExportInnerJobOrderMapper;
import com.fantechs.provider.wms.inner.service.WmsDataExportInnerJobOrderService;
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
public class WmsDataExportInnerJobOrderServiceImpl extends BaseService<WmsDataExportInnerJobOrderDto> implements WmsDataExportInnerJobOrderService {

    @Resource
    private WmsDataExportInnerJobOrderMapper wmsDataExportInnerJobOrderMapper;
    @Resource
    FiveringFeignApi fiveringFeignApi;

    @Override
    public List<WmsDataExportInnerJobOrderDto> findExportData(Map<String, Object> map) {
        return wmsDataExportInnerJobOrderMapper.findExportData(map);
    }

    @Override
    public String writeDeliveryDetails(WmsInnerJobOrder wmsInnerJobOrder) {
        String jsonVoiceArray="";
        String projectID="3919";
        Map<String, Object> map=new HashMap<>();
        map.put("jobOrderId",wmsInnerJobOrder.getJobOrderId());
        List<WmsDataExportInnerJobOrderDto> listDto=findExportData(map);
        jsonVoiceArray= JsonUtils.objectToJson(listDto);
        String s0=jsonVoiceArray.replaceAll("packingOrderSummaryDetId","WMSKey");
        String s1=s0.replaceAll("option1","PPGUID");
        String s2=s1.replaceAll("contractCode","合同号");
        String s3=s2.replaceAll("purchaseReqOrderCode","请购单号");
        String s4=s3.replaceAll("despatchBatch","物流批次号");
        String s5=s4.replaceAll("cartonCode","箱号");
        String s6=s5.replaceAll("materialCode","材料编码");
        String s7=s6.replaceAll("locationNum","位号");
        String s8=s7.replaceAll("dominantTermCode","主项号");
        String s9=s8.replaceAll("deviceCode","装置号");
        String s10=s9.replaceAll("putawayQty","到货量");
        String s11=s10.replaceAll("storageCode","货架编号");
        String s12=s11.replaceAll("remark","到货备注");
        String s13=s12.replaceAll("recordTime","登记时间");
        String s14=s13.replaceAll("recordUser","登记人");
        String s15=s14.replaceAll("innerTime","入库校验时间");
        String s16=s15.replaceAll("innerSureTime","入库确认时间");

        ResponseEntity<String>  responseEntityResult=fiveringFeignApi.writeDeliveryDetails(s16,projectID);

        return responseEntityResult.getData();
    }
}
