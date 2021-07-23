package com.fantechs.provider.baseapi.esop.util;

import com.fantechs.common.base.entity.security.SysApiLog;
import com.fantechs.common.base.utils.DateUtils;
import com.fantechs.provider.api.security.service.SecurityFeignApi;
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
    private SecurityFeignApi securityFeignApi;
    /**
     * 第一位1表示打印标签 发送打印字节
     * @param result
     * @param type
     * @param orgId
     * @param responseData
     * @param requestParameter
     */
    public void  addlog(Byte result,Byte type,Long orgId,String responseData,String requestParameter) throws ParseException {
        SysApiLog sysApiLog = new SysApiLog();
        sysApiLog.setThirdpartySysName("新宝ESOP");
        sysApiLog.setCallResult(result);
        sysApiLog.setCallType(type);
        sysApiLog.setApiModule("ocean-restapi");
        sysApiLog.setOrgId(orgId);
        sysApiLog.setRequestTime(new Date());
        sysApiLog.setResponseTime(new Date());
        sysApiLog.setResponseData(responseData);
        sysApiLog.setRequestParameter(requestParameter);
        securityFeignApi.add(sysApiLog);
    }
}
