package com.fantechs.provider.materialapi.imes.service;


import com.fantechs.common.base.general.dto.restapi.RestapiWorkOrderApiDto;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.text.ParseException;
import java.util.List;

@WebService(name = "TestService", // 暴露服务名称
        targetNamespace = "http://test.imes.materialapi.provider.fantechs.com"// 命名空间,一般是接口的包名倒序
)
public interface SapTestService {


    @WebMethod
    String testService(List<RestapiWorkOrderApiDto> restapiWorkOrderApiDtos) throws ParseException;

}
