package com.fantechs.provider.materialapi.imes.service.impl;


import com.fantechs.common.base.general.dto.restapi.RestapiWorkOrderApiDto;
import com.fantechs.common.base.general.dto.restapi.RestapiWorkOrderBomApiDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderBom;
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
    public String saveWorkOrder(RestapiWorkOrderApiDto restapiWorkOrderApiDto) throws ParseException {
        String check = check(restapiWorkOrderApiDto);
        if(!check.equals("1")){
            return check;
        }
        System.out.println("----restapiWorkOrderApiDto---"+restapiWorkOrderApiDto);
        SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
        searchBaseMaterial.setMaterialCode(restapiWorkOrderApiDto.getMATNR());
        ResponseEntity<List<BaseMaterial>> baseMaterialList = baseFeignApi.findList(searchBaseMaterial);
        if(StringUtils.isEmpty(baseMaterialList.getData()))   return "为查询到对应物料号";
        BaseMaterial baseMaterial = baseMaterialList.getData().get(0);
        MesPmWorkOrder mesPmWorkOrder = new MesPmWorkOrder();
        mesPmWorkOrder.setWorkOrderCode(restapiWorkOrderApiDto.getAUFNR());
        mesPmWorkOrder.setPlanStartTime(DateUtils.getStrToDate("yyyyMMdd",restapiWorkOrderApiDto.getGSTRP()));
        mesPmWorkOrder.setPlanEndTime(DateUtils.getStrToDate("yyyyMMdd",restapiWorkOrderApiDto.getGLTRP()));
        mesPmWorkOrder.setCreateTime(DateUtils.getStrToDate("yyyyMMdd",restapiWorkOrderApiDto.getERDAT()));
        mesPmWorkOrder.setMaterialId(baseMaterial.getMaterialId());
        ResponseEntity mesPmWorkOrderDate = pmFeignApi.updateById(mesPmWorkOrder);

        for(RestapiWorkOrderBomApiDto dto : restapiWorkOrderApiDto.getWorkOrderBom()) {

          //  ResponseEntity oldMesPmWorkOrderBom = pmFeignApi.findMesPmWorkOrderBom(mesPmWorkOrderBom.getWorkOrderBomId());
            MesPmWorkOrderBom mesPmWorkOrderBom = new MesPmWorkOrderBom();
            mesPmWorkOrderBom.setUsageQty(new BigDecimal(dto.getMENGE()));
            System.out.println("---mesPmWorkOrder.getWorkOrderId()---"+mesPmWorkOrder.getWorkOrderId());
            mesPmWorkOrderBom.setWorkOrderId(mesPmWorkOrder.getWorkOrderId());
            pmFeignApi.addMesPmWorkOrderBom(mesPmWorkOrderBom);
            /*if(StringUtils.isEmpty(oldMesPmWorkOrderBom.getData())){
                if(StringUtils.isEmpty(mesPmWorkOrderBom.getCreateTime())) mesPmWorkOrderBom.setCreateTime(new Date());
                pmFeignApi.addMesPmWorkOrderBom(mesPmWorkOrderBom);
            }else{
                pmFeignApi.updateMesPmWorkOrderBom(mesPmWorkOrderBom);
            }*/
        }
        return "success";
    }


    public String check(RestapiWorkOrderApiDto restapiWorkOrderApiDto) {

        if(StringUtils.isEmpty(restapiWorkOrderApiDto))
            return "请求失败,参数为空";
        if(StringUtils.isEmpty(restapiWorkOrderApiDto.getAUFNR()))
            return "请求失败,工单号不能为空";
        if(StringUtils.isEmpty(restapiWorkOrderApiDto.getMATNR()))
            return "请求失败,物料号不能为空";

        return "1";
    }

   /* @Override
    public String findWorkOrder(SearchMesPmWorkOrder searchMesPmWorkOrder) {

        if(StringUtils.isEmpty(searchMesPmWorkOrder)){
            return "fail";
        }
       ResponseEntity<List<MesPmWorkOrderDto>> workOrderList = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder);
        if(StringUtils.isEmpty(workOrderList.getData())){
            return "fail";
        }
        return "success";
    }*/

}
