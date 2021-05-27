package com.fantechs.provider.materialapi.imes.service.impl;

import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
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


    @Override
    public String saveWorkOrder(MesPmWorkOrder mesPmWorkOrder) {
        String check = check(mesPmWorkOrder);
        if(check != "checked"){
            return check;
        }

        ResponseEntity<MesPmWorkOrder> mesPmWorkOrderDate = pmFeignApi.workOrderDetail(mesPmWorkOrder.getWorkOrderId());
        pmFeignApi.updateById(mesPmWorkOrder);
        return "success";
    }

    public String check(MesPmWorkOrder mesPmWorkOrder) {
        if(StringUtils.isEmpty(mesPmWorkOrder))
            return "fail,参数为空";
        if(StringUtils.isEmpty(mesPmWorkOrder.getWorkOrderId()))
            return "fail,订单id不能为空";
        return "checked";
    }

    @Override
    public String findWorkOrder(SearchMesPmWorkOrder searchMesPmWorkOrder) {


        if(StringUtils.isEmpty(searchMesPmWorkOrder)){
            return "fail";
        }
       ResponseEntity<List<MesPmWorkOrderDto>> workOrderList = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder);
        if(StringUtils.isEmpty(workOrderList.getData())){
            return "fail222";
        }
        return "success";
    }
}
