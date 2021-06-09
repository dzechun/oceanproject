package com.fantechs.provider.materialapi.imes.service.impl;


import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.restapi.RestapiWorkOrderApiDto;
import com.fantechs.common.base.general.dto.restapi.RestapiWorkOrderBomApiDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderBom;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.DateUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.materialapi.imes.service.MaterialService;

import javax.annotation.Resource;
import javax.jws.WebService;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@WebService(serviceName = "Materialservice", // 与接口中指定的name一致
        targetNamespace = "http://imes.materialapi.provider.fantechs.com", // 与接口中的命名空间一致,一般是接口的包名倒
        endpointInterface = "com.fantechs.provider.materialapi.imes.service.MaterialService"// 接口地址
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
