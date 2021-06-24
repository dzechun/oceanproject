package com.fantechs.provider.materialapi.imes.service.impl;


import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderBomDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.restapi.RestapiWorkOrderApiDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseOrganization;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProLine;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderBom;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrderBom;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.DateUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.materialapi.imes.service.SapTestService;
import com.fantechs.provider.materialapi.imes.service.SapWorkOrderService;

import javax.annotation.Resource;
import javax.jws.WebService;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@WebService(serviceName = "TestService", // 与接口中指定的name一致
        targetNamespace = "http://test.imes.materialapi.provider.fantechs.com", // 与接口中的命名空间一致,一般是接口的包名倒
        endpointInterface = "com.fantechs.provider.materialapi.imes.service.SapTestService"// 接口地址
)
public class SapTestServiceImpl implements SapTestService {

    @Resource
    private PMFeignApi pmFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public String testService(List<RestapiWorkOrderApiDto> restapiWorkOrderApiDtos) throws ParseException {
        if(StringUtils.isEmpty(restapiWorkOrderApiDtos)) return "工单参数为空";

        for(RestapiWorkOrderApiDto restapiWorkOrderApiDto : restapiWorkOrderApiDtos) {
            Long orgId = getOrId();
            //保存或更新工单
            MesPmWorkOrder mesPmWorkOrder = new MesPmWorkOrder();
            mesPmWorkOrder.setOrgId(orgId);
            mesPmWorkOrder.setMaterialId(getBaseMaterial(restapiWorkOrderApiDto.getMATNR()).getMaterialId());
            mesPmWorkOrder.setWorkOrderQty(new BigDecimal(restapiWorkOrderApiDto.getGAMNG()));
            mesPmWorkOrder.setWorkOrderCode("12345");
            pmFeignApi.updateById(mesPmWorkOrder);
        }
        return "success";
    }


    public Long getOrId() {
        SearchBaseOrganization searchBaseOrganization = new SearchBaseOrganization();
        searchBaseOrganization.setOrganizationName("雷赛");
        ResponseEntity<List<BaseOrganizationDto>> organizationList = baseFeignApi.findOrganizationList(searchBaseOrganization);
        if (StringUtils.isEmpty(organizationList.getData())) throw new BizErrorException("未查询到对应组织");
        return organizationList.getData().get(0).getOrganizationId();
    }

    public BaseMaterial getBaseMaterial(String materialCode){
        SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
        searchBaseMaterial.setMaterialCode(materialCode);
        searchBaseMaterial.setOrganizationId(getOrId());
        ResponseEntity<List<BaseMaterial>> parentMaterialList = baseFeignApi.findSmtMaterialList(searchBaseMaterial);
        if(StringUtils.isEmpty(parentMaterialList.getData()))
            throw new BizErrorException("未查询到对应的物料："+materialCode);
        return parentMaterialList.getData().get(0);
    }
}
