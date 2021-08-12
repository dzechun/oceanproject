package com.fantechs.provider.materialapi.imes.service;


import com.fantechs.common.base.general.dto.restapi.RestapiChkLogUserInfoApiDto;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.text.ParseException;
import java.util.List;

@WebService(name = "Chk_LogUserInfoService", // 暴露服务名称
        targetNamespace = "http://imes.materialapi.provider.fantechs.com"// 命名空间,一般是接口的包名倒序
)
public interface Chk_LogUserInfoService {

    @WebMethod
    String Chk_LogUserInfo(RestapiChkLogUserInfoApiDto restapiChkLogUserInfoApiDto) throws ParseException;

}
