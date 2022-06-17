package com.fantechs.provider.baseapi.esop.util;

import com.fantechs.common.base.entity.security.SysApiLog;
import com.fantechs.provider.api.auth.service.AuthFeignApi;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.ParseException;
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
    public void  addlog(Byte result,Byte type,Long orgId,String responseData,String requestParameter) throws ParseException {
        SysApiLog sysApiLog = new SysApiLog();
        sysApiLog.setThirdpartySysName("新宝ESOP");
        sysApiLog.setCallResult(result); //调用结果 0-失败 1-成功
        sysApiLog.setCallType(type); //调用类型(1-主动 2-被动)
        sysApiLog.setApiModule("ocean-esop-baseapi");
        sysApiLog.setOrgId(orgId);
        sysApiLog.setRequestTime(new Date());
        sysApiLog.setResponseTime(new Date());
        sysApiLog.setResponseData(responseData);
        sysApiLog.setRequestParameter(requestParameter);
        securityFeignApi.add(sysApiLog);
    }
}
