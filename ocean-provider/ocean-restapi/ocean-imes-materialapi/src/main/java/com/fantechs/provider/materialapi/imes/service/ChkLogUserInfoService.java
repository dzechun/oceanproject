package com.fantechs.provider.materialapi.imes.service;


import com.fantechs.common.base.general.dto.restapi.RestapiChkLogUserInfoApiDto;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.text.ParseException;
import java.util.List;

@WebService(name = "ChkLogUserInfoService", // 暴露服务名称
        targetNamespace = "http://ChkLogUserInfo.imes.materialapi.provider.fantechs.com"// 命名空间,一般是接口的包名倒序
)
public interface ChkLogUserInfoService {

    @WebMethod
    String ChkLogUserInfo(RestapiChkLogUserInfoApiDto restapiChkLogUserInfoApiDto) throws ParseException;

}
