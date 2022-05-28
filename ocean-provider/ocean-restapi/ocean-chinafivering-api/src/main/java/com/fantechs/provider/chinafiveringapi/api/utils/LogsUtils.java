package com.fantechs.provider.chinafiveringapi.api.utils;

import com.fantechs.common.base.entity.security.SysApiLog;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component
public class LogsUtils {

    @Resource
    private AuthFeignApi securityFeignApi;
    /**
     * 日志
     * @param result
     * @param type
     * @param orgId
     * @param responseData
     * @param requestParameter
     */
    public void  addlog(Byte result,Byte type,Long orgId,String responseData,String requestParameter,String remark){
        SysApiLog sysApiLog = new SysApiLog();
        sysApiLog.setThirdpartySysName("五环回传接口");
        sysApiLog.setCallResult(result);
        sysApiLog.setCallType(type);
        sysApiLog.setApiModule("ocean-chinafivering-api");
        sysApiLog.setOrgId(orgId);
        sysApiLog.setRequestTime(new Date());
        sysApiLog.setResponseTime(new Date());
        sysApiLog.setResponseData(responseData);
        sysApiLog.setRequestParameter(requestParameter);
        sysApiLog.setRemark(remark);
        securityFeignApi.add(sysApiLog);
    }
}

