package com.fantechs.provider.materialapi.imes.service.impl;

import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
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
    private PMFeignApi pmFeignApi;

    @Override
    public String testMethod(String testName) {
        return "你好," + testName;
    }

    @Override
    public String syncMaterial(List<BaseMaterial> baseMaterials) {

        if(baseMaterials.size() <= 0){
            return "fail";
        }
        return "success";
    }

    @Override
    public String findWorkOrder(SearchMesPmWorkOrder searchMesPmWorkOrder) {

        if(StringUtils.isEmpty(searchMesPmWorkOrder)){
            return "fail";
        }
        ResponseEntity<List<MesPmWorkOrderDto>> workOrderList = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder);
        return "success"+ workOrderList.getData().toString();
    }
}
