package com.fantechs.provider.materialapi.imes.service.impl;


import com.fantechs.common.base.general.dto.restapi.RestapiWorkOrderApiDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderBom;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.materialapi.imes.service.MaterialService;

import javax.annotation.Resource;
import javax.jws.WebService;
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
    public String saveWorkOrder(RestapiWorkOrderApiDto restapiWorkOrderApiDto) {
        String check = check(restapiWorkOrderApiDto);
        if(!check.equals("1")){
            return check;
        }
        pmFeignApi.updateById(restapiWorkOrderApiDto.getMesPmWorkOrder());
        for(MesPmWorkOrderBom mesPmWorkOrderBom : restapiWorkOrderApiDto.getMesPmWorkOrderBoms()) {
            if(StringUtils.isEmpty(mesPmWorkOrderBom.getWorkOrderBomId()))  return "请求失败,工单Bom的id不能为空";
            ResponseEntity oldMesPmWorkOrderBom = pmFeignApi.findMesPmWorkOrderBom(mesPmWorkOrderBom.getWorkOrderBomId());
            if(StringUtils.isEmpty(oldMesPmWorkOrderBom.getData())){
                if(StringUtils.isEmpty(mesPmWorkOrderBom.getCreateTime())) mesPmWorkOrderBom.setCreateTime(new Date());
                pmFeignApi.addMesPmWorkOrderBom(mesPmWorkOrderBom);
            }else{
                pmFeignApi.updateMesPmWorkOrderBom(mesPmWorkOrderBom);
            }
        }
        return "success";
    }


    public String check(RestapiWorkOrderApiDto restapiWorkOrderApiDto) {

        if(StringUtils.isEmpty(restapiWorkOrderApiDto))
            return "请求失败,参数为空";
        if(StringUtils.isEmpty(restapiWorkOrderApiDto.getMesPmWorkOrder()))
            return "请求失败,工单参数为空";
        if(StringUtils.isEmpty(restapiWorkOrderApiDto.getMesPmWorkOrderBoms()))
            return "请求失败,工单Bom参数为空";
        if(StringUtils.isEmpty(restapiWorkOrderApiDto.getMesPmWorkOrder().getWorkOrderId()))
            return "请求失败,工单id不能为空";
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
