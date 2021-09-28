package com.fantechs.provider.guest.eng.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.restapi.EngReportInnerJobOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.guest.fivering.FiveringFeignApi;
import com.fantechs.provider.guest.eng.mapper.EngReportInnerJobOrderMapper;
import com.fantechs.provider.guest.eng.service.EngReportInnerJobOrderService;
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
public class EngReportInnerJobOrderServiceImpl implements EngReportInnerJobOrderService {

    @Resource
    private EngReportInnerJobOrderMapper engReportInnerJobOrderMapper;
    @Resource
    private FiveringFeignApi fiveringFeignApi;


    @Override
    public String reportInnerJobOrder(WmsInnerJobOrder wmsInnerJobOrder){
        //获取当前操作用户
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        String jsonVoiceArray="";
        String projectID="3919";
        Map<String, Object> map=new HashMap<>();
        map.put("jobOrderId",wmsInnerJobOrder.getJobOrderId());
        List<EngReportInnerJobOrderDto> innerJobOrders = engReportInnerJobOrderMapper.findInnerJobOrder(map);

        for (EngReportInnerJobOrderDto item : innerJobOrders) {
            //设置登记人
            if(StringUtils.isNotEmpty(user)) {
                item.setCreateUserName(user.getUserName());
            }
        }

        jsonVoiceArray= JsonUtils.objectToJson(innerJobOrders);
        String s0=jsonVoiceArray.replaceAll("jobOrderDetId","WMSKey");
        String s1=s0.replaceAll("option1","PPGUID");
        String s2=s1.replaceAll("option2","PSGUID");
        String s3=s2.replaceAll("contractCode","合同号");
        String s4=s3.replaceAll("purchaseReqOrderCode","请购单号");
        String s5=s4.replaceAll("materialCode","材料编码");
        String s6=s5.replaceAll("locationNum","位号");
        String s7=s6.replaceAll("dominantTermCode","主项号");
        String s8=s7.replaceAll("deviceCode","装置号");
        String s9=s8.replaceAll("planQty","变化量");
        String s10=s9.replaceAll("oldStorageId","旧DHGUID");
        String s11=s10.replaceAll("newStorageId","新DHGUID");
        String s12=s11.replaceAll("createTime","登记时间");
        String s13=s12.replaceAll("createUserName","登记人");

        //ResponseEntity<String>  responseEntityResult=fiveringFeignApi.writeIssueDetails (s13,projectID);
        ResponseEntity<String>  responseEntityResult=fiveringFeignApi.writeMoveInventoryDetails (s13,projectID);


        return responseEntityResult.getData();
    }


}
