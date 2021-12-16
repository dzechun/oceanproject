package com.fantechs.provider.guest.meidi.service.impl;

import com.fantechs.common.base.entity.security.SysApiLog;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.response.ControllerUtil;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.HTTPUtils;
import com.fantechs.common.base.utils.JsonUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
import com.fantechs.provider.guest.meidi.entity.MeterialPrepation;
import com.fantechs.provider.guest.meidi.entity.MeterialPrepationRequest;
import com.fantechs.provider.guest.meidi.service.MeterialPrepationService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/10/26.
 */
@Service
public class MeterialPrepationServiceImpl implements MeterialPrepationService {

    @Resource
    private SecurityFeignApi securityFeignApi;

    private String completedUrl= "http://127.0.0.1:9600/FCS/PDA/MeterialPrepationCompleted";
    private String canceUrl= "http://127.0.0.1:9600/FCS/PDA/MeterialPrepationCance";

    @Override
    public int send(MeterialPrepation meterialPrepation) {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if(StringUtils.isEmpty(meterialPrepation.getStorageCode()) || StringUtils.isEmpty())
            throw  new BizErrorException("库位编码或接口请求类型不能为空");
        MeterialPrepationRequest req = new MeterialPrepationRequest();
        Map<String,Object> map = new HashMap<>();
    //    req.setRequestCode();
        String data = "{\"locationCode\":\""+meterialPrepation.getStorageCode()+"\",\""+user.getNickName()+"\":\""+user.getNickName()+"\"}";
        System.out.println("---------请求参数-------------"+data);
        req.setRequestData(data);
        if("1".equals(meterialPrepation.getType())){
            req.setRequsetName("MeterialPreparationCompleted");
            String result = HTTPUtils.postMap(completedUrl, new HashMap<>(), ControllerUtil.dynamicConditionByEntity(req));
            if(StringUtils.isNotEmpty(result)){
                map =  JsonUtils.jsonToMap(result);
                System.out.println("---------请求结果map1-------------"+map);
            }
        }else if("2".equals(meterialPrepation.getType())){
            req.setRequsetName("MeterialPreparationCancel");
            String result = HTTPUtils.postMap(canceUrl, new HashMap<>(), ControllerUtil.dynamicConditionByEntity(req));
            if(StringUtils.isNotEmpty(result)){
                map = JsonUtils.jsonToMap(result);
                System.out.println("---------请求结果map2------------"+map);
            }
        }else{
            throw  new BizErrorException("请求接口类型错误");
        }

        //保存日志
        if("success".equals(map.get("responseMessag"))){
            addlog((byte)1,user.getOrganizationId(),ControllerUtil.dynamicConditionByEntity(req).toString(),map.get("requestData").toString());
        }else{
            addlog((byte)2,user.getOrganizationId(),ControllerUtil.dynamicConditionByEntity(req).toString(),map.get("requestData").toString());
        }
        return 1;
    }


    /**
     * 日志
     * @param result
     * @param orgId
     * @param responseData
     * @param requestParameter
     */
    public void  addlog(Byte result,Long orgId,String responseData,String requestParameter){
        SysApiLog sysApiLog = new SysApiLog();
        sysApiLog.setThirdpartySysName("美的");
        sysApiLog.setCallResult(result);
        sysApiLog.setCallType((byte)1);
        sysApiLog.setApiModule("ocean-guest-meidi");
        sysApiLog.setOrgId(orgId);
        sysApiLog.setRequestTime(new Date());
        sysApiLog.setResponseTime(new Date());
        sysApiLog.setResponseData(responseData);
        sysApiLog.setRequestParameter(requestParameter);
        securityFeignApi.add(sysApiLog);
    }
}
