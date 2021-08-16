package com.fantechs.provider.materialapi.imes.service;


import com.fantechs.common.base.general.dto.restapi.RestapiChkSNRoutingApiDto;
import com.fantechs.common.base.general.dto.restapi.RestapiSNDataTransferApiDto;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.text.ParseException;

@WebService(name = "SN_Data_TransferService", // 暴露服务名称
        targetNamespace = "http://SN_Data_Transfer.imes.materialapi.provider.fantechs.com"// 命名空间,一般是接口的包名倒序
)
public interface SN_Data_TransferService {

    @WebMethod
    String SN_Data_Transfer(RestapiSNDataTransferApiDto restapiSNDataTransferApiDto) throws Exception;

}
