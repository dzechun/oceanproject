package com.fantechs.provider.materialapi.imes.service;


import com.fantechs.common.base.general.dto.restapi.RestapiWorkOrderApiDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService(name = "MaterialService", // 暴露服务名称
        targetNamespace = "http://imes.materialapi.provider.fantechs.com"// 命名空间,一般是接口的包名倒序
)
public interface MaterialService {

    @WebMethod
    String testMethod(String testName);

    @WebMethod
    String syncMaterial(List<BaseMaterial> baseMaterials);

    @WebMethod
    String findWorkOrder(SearchMesPmWorkOrder searchMesPmWorkOrder);

    @WebMethod
    String saveWorkOrder(RestapiWorkOrderApiDto restapiWorkOrderApiDto);


}
