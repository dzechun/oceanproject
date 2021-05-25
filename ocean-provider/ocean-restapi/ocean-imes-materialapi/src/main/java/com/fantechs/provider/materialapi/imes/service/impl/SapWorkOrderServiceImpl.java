package com.fantechs.provider.materialapi.imes.service.impl;

import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.materialapi.imes.service.SapWorkOrderService;

import javax.annotation.Resource;
import javax.jws.WebService;
import java.util.List;

@WebService(serviceName = "SapWorkOrderService", // 与接口中指定的name一致
        targetNamespace = "http://imes.materialapi.provider.fantechs.com", // 与接口中的命名空间一致,一般是接口的包名倒
        endpointInterface = "com.fantechs.provider.materialapi.imes.service.SapWorkOrderService"// 接口地址
)
public class SapWorkOrderServiceImpl implements SapWorkOrderService {

    @Resource
    private PMFeignApi pmFeignApi;

    @Override
    public String findWorkOrder(SearchMesPmWorkOrder searchMesPmWorkOrder) {

        if(StringUtils.isEmpty(searchMesPmWorkOrder)){
            return "fail";
        }

        ResponseEntity<List<MesPmWorkOrderDto>> workOrderList = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder);
        return "success"+ workOrderList.getData().toString();
    }

}
