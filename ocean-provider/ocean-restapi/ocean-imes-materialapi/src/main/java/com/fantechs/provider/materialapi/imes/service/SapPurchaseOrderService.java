package com.fantechs.provider.materialapi.imes.service;


import com.fantechs.common.base.general.dto.restapi.RestapiPurchaseOrderApiDto;
import com.fantechs.common.base.general.dto.restapi.RestapiWorkOrderApiDto;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.text.ParseException;
import java.util.List;

@WebService(name = "PurchaseOrderService", // 暴露服务名称
        targetNamespace = "http://purchaseOrderService.imes.materialapi.provider.fantechs.com"// 命名空间,一般是接口的包名倒序
)
public interface SapPurchaseOrderService {


    @WebMethod
    String purchaseOrder(List<RestapiPurchaseOrderApiDto> purchaseOrderApiDtos) throws ParseException;

}
