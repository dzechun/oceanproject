package com.fantechs.provider.materialapi.imes.service;


import com.fantechs.common.base.general.dto.restapi.RestapiChkSNRoutingApiDto;
import com.fantechs.common.base.general.dto.restapi.RestapiSNDataTransferApiDto;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.text.ParseException;

@WebService(name = "SnDataTransferService", // 暴露服务名称
        targetNamespace = "http://SnDataTransfer.imes.materialapi.provider.fantechs.com"// 命名空间,一般是接口的包名倒序
)
public interface SnDataTransferService {

    @WebMethod
    String SnDataTransfer(RestapiSNDataTransferApiDto restapiSNDataTransferApiDto) throws Exception;

}
