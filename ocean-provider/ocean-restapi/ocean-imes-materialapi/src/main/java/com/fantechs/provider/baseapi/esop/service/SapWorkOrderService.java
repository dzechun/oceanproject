package com.fantechs.provider.baseapi.esop.service;


import com.fantechs.common.base.general.dto.restapi.RestapiWorkOrderApiDto;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.text.ParseException;
import java.util.List;

@WebService(name = "WorkOrderService", // 暴露服务名称
        targetNamespace = "http://workOrder.imes.materialapi.provider.fantechs.com"// 命名空间,一般是接口的包名倒序
)
public interface SapWorkOrderService {


    @WebMethod
    String saveWorkOrder(List<RestapiWorkOrderApiDto> restapiWorkOrderApiDtos) throws ParseException;

}
