package com.fantechs.provider.materialapi.imes.service;


import com.fantechs.common.base.general.dto.restapi.RestapiChkLogUserInfoApiDto;
import com.fantechs.common.base.general.dto.restapi.RestapiChkSNRoutingApiDto;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.text.ParseException;

@WebService(name = "ChkSnRoutingService", // 暴露服务名称
        targetNamespace = "http://ChkSnRouting.imes.materialapi.provider.fantechs.com"// 命名空间,一般是接口的包名倒序
)
public interface ChkSnRoutingService {

    @WebMethod
    String ChkSnRouting(RestapiChkSNRoutingApiDto restapiChkSNRoutingApiDto) throws ParseException;

}
