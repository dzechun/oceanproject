package com.fantechs.provider.materialapi.imes.service.impl;

import com.fantechs.common.base.entity.basic.SmtMaterial;
import com.fantechs.provider.api.imes.basic.BasicFeignApi;

import com.fantechs.provider.materialapi.imes.service.MaterialService;

import javax.annotation.Resource;
import javax.jws.WebService;
import java.util.List;

@WebService(serviceName = "MaterialService", // 与接口中指定的name一致
        targetNamespace = "http://imes.materialapi.provider.fantechs.com", // 与接口中的命名空间一致,一般是接口的包名倒
        endpointInterface = "com.fantechs.provider.materialapi.imes.service.MaterialService"// 接口地址
)
public class MaterialServiceImpl implements MaterialService {

    @Resource
    private BasicFeignApi basicFeignApi;

    @Override
    public String testMethod(String testName) {
        return "你好," + testName;
    }

    @Override
    public String syncMaterial(List<SmtMaterial> smtMaterials) {

        if(smtMaterials.size() <= 0){
            return "fail";
        }
        //新增物料
        //basicFeignApi.batchUpdateSmtMaterial(smtMaterials);

        return "success";
    }

}
