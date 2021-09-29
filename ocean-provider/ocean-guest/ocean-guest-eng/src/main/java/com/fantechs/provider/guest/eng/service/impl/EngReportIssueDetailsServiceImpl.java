package com.fantechs.provider.guest.eng.service.impl;

import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.general.dto.restapi.EngReportDeliveryOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.guest.fivering.FiveringFeignApi;
import com.fantechs.provider.guest.eng.mapper.EngReportIssueDetailsMapper;
import com.fantechs.provider.guest.eng.service.EngReportIssueDetailsService;
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
public class EngReportIssueDetailsServiceImpl implements EngReportIssueDetailsService {

    @Resource
    private EngReportIssueDetailsMapper engReportIssueDetailsMapper;
    @Resource
    private FiveringFeignApi fiveringFeignApi;


    @Override
    public String reportIssueDetails(WmsInnerJobOrder wmsInnerJobOrder){
        //获取当前操作用户
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();

        String jsonVoiceArray="";
        String projectID="3919";
        Map<String, Object> map=new HashMap<>();
        map.put("jobOrderId",wmsInnerJobOrder.getJobOrderId());
        List<EngReportDeliveryOrderDto> deliveryOrderOrder = engReportIssueDetailsMapper.findDeliveryOrderOrder(map);

        for (EngReportDeliveryOrderDto item : deliveryOrderOrder) {
            //设置登记人
            if(StringUtils.isNotEmpty(user)) {
                item.setCreateUserName(user.getUserName());
            }
        }

        jsonVoiceArray= JsonUtils.objectToJson(deliveryOrderOrder);
        String s0=jsonVoiceArray.replaceAll("deliveryOrderDetId","WMSKey");
        String s1=s0.replaceAll("option2","ISGUID");
        String s2=s1.replaceAll("option11","实发量");
        String s3=s2.replaceAll("option1","IDGUID");
        String s4=s3.replaceAll("remark","发料备注");
        String s5=s4.replaceAll("createTime","登记时间");
        String s6=s5.replaceAll("createUserName","登记人");

        ResponseEntity<String>  responseEntityResult=fiveringFeignApi.writeIssueDetails (s6,projectID);

        return responseEntityResult.getData();
    }


}
