package com.fantechs.provider.baseapi.esop.service.impl;


import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.baseapi.esop.service.MaterialService;

import javax.annotation.Resource;
import javax.jws.WebService;
import java.util.List;

@WebService(serviceName = "Materialservice", // 与接口中指定的name一致
        targetNamespace = "http://imes.materialapi.provider.fantechs.com", // 与接口中的命名空间一致,一般是接口的包名倒
        endpointInterface = "com.fantechs.provider.baseapi.esop.service.MaterialService"// 接口地址
)
public class MaterialServiceImpl implements MaterialService {

    @Resource
    private BaseFeignApi baseFeignApi;


    @Override
    public String testMethod(String testName) {
        return "你好," + testName;
    }

    @Override
    public String syncMaterial(List<BaseMaterial> baseMaterials) {

        if(baseMaterials.size() <= 0){
            return "fail";
        }
        //新增物料
        baseFeignApi.batchUpdateSmtMaterial(baseMaterials);
        return "success";
    }


}
