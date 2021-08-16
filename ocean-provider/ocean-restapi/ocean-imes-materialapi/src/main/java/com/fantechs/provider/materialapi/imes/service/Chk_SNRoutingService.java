package com.fantechs.provider.materialapi.imes.service;


import com.fantechs.common.base.general.dto.restapi.RestapiChkLogUserInfoApiDto;
import com.fantechs.common.base.general.dto.restapi.RestapiChkSNRoutingApiDto;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.text.ParseException;

@WebService(name = "Chk_SNRoutingService", // 暴露服务名称
        targetNamespace = "http://Chk_SNRouting.imes.materialapi.provider.fantechs.com"// 命名空间,一般是接口的包名倒序
)
public interface Chk_SNRoutingService {

    @WebMethod
    String Chk_SNRouting(RestapiChkSNRoutingApiDto restapiChkSNRoutingApiDto) throws ParseException;

}
