package com.fantechs.provider.materialapi.imes.service;


import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;

import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(name = "SapWorkOrderService", // 暴露服务名称
        targetNamespace = "http://imes.materialapi.provider.fantechs.com"// 命名空间,一般是接口的包名倒序
)
public interface SapWorkOrderService {

    @WebMethod
    String findWorkOrder(SearchMesPmWorkOrder searchMesPmWorkOrder);
}
