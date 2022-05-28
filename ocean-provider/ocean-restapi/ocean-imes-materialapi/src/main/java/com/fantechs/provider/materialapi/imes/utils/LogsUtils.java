package com.fantechs.provider.materialapi.imes.utils;

import com.fantechs.common.base.entity.security.SysApiLog;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author Mr.Lei
 * @create 2021/2/24
 */
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
    public void  addlog(Byte result,Byte type,Long orgId,String responseData,String requestParameter){
        SysApiLog sysApiLog = new SysApiLog();
        sysApiLog.setThirdpartySysName("雷赛");
        sysApiLog.setCallResult(result);
        sysApiLog.setCallType(type);
        sysApiLog.setApiModule("ocean-materialapi");
        sysApiLog.setOrgId(orgId);
        sysApiLog.setRequestTime(new Date());
        sysApiLog.setResponseTime(new Date());
        sysApiLog.setResponseData(responseData);
        sysApiLog.setRequestParameter(requestParameter);
        securityFeignApi.add(sysApiLog);
    }
}
