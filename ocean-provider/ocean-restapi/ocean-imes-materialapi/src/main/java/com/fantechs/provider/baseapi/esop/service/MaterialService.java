package com.fantechs.provider.baseapi.esop.service;


import com.fantechs.common.base.general.dto.restapi.RestapiWorkOrderApiDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.text.ParseException;
import java.util.List;

@WebService(name = "MaterialService", // 暴露服务名称
        targetNamespace = "http://imes.materialapi.provider.fantechs.com"// 命名空间,一般是接口的包名倒序
)
public interface MaterialService {

    @WebMethod(action="http://imes.materialapi.provider.fantechs.com/MaterialService")
    String testMethod(@WebParam(name ="testName")String testName);

    @WebMethod
    String syncMaterial(List<BaseMaterial> baseMaterials);

}
