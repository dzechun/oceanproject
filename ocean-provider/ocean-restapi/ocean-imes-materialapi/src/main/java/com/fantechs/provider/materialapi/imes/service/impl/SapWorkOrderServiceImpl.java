package com.fantechs.provider.materialapi.imes.service.impl;


import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderBomDto;
import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.dto.restapi.RestapiWorkOrderApiDto;
import com.fantechs.common.base.general.dto.restapi.RestapiWorkOrderBomApiDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseMaterial;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderBom;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.search.SearchMesPmWorkOrderBom;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.DateUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.materialapi.imes.service.SapWorkOrderService;

import javax.annotation.Resource;
import javax.jws.WebService;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@WebService(serviceName = "WorkOrderService", // 与接口中指定的name一致
        targetNamespace = "http://workOrder.imes.materialapi.provider.fantechs.com", // 与接口中的命名空间一致,一般是接口的包名倒
        endpointInterface = "com.fantechs.provider.materialapi.imes.service.SapWorkOrderService"// 接口地址
)
public class SapWorkOrderServiceImpl implements SapWorkOrderService {

    @Resource
    private PMFeignApi pmFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public String saveWorkOrder(RestapiWorkOrderApiDto restapiWorkOrderApiDto) throws ParseException {
        String check = check(restapiWorkOrderApiDto);
        if(!check.equals("1")){
            return check;
        }
        //获取物料
        SearchBaseMaterial searchBaseMaterial = new SearchBaseMaterial();
        searchBaseMaterial.setMaterialCode(restapiWorkOrderApiDto.getMATNR());
        ResponseEntity<List<BaseMaterial>> baseMaterialList = baseFeignApi.findList(searchBaseMaterial);
        if(StringUtils.isEmpty(baseMaterialList.getData()))   return "未查询到对应物料号";
        BaseMaterial baseMaterial = baseMaterialList.getData().get(0);

        //保存或更新工单
        MesPmWorkOrder mesPmWorkOrder = new MesPmWorkOrder();
        mesPmWorkOrder.setWorkOrderCode(restapiWorkOrderApiDto.getAUFNR());
        System.out.println("------restapiWorkOrderApiDto.getGSTRP()------"+restapiWorkOrderApiDto.getGSTRP());
        if(StringUtils.isNotEmpty(restapiWorkOrderApiDto.getGSTRP()))
            mesPmWorkOrder.setPlanStartTime(DateUtils.getStrToDate("yyyyMMdd",restapiWorkOrderApiDto.getGSTRP()));
        if(StringUtils.isNotEmpty(restapiWorkOrderApiDto.getGLTRP()))
            mesPmWorkOrder.setPlanEndTime(DateUtils.getStrToDate("yyyyMMdd",restapiWorkOrderApiDto.getGLTRP()));
        if(StringUtils.isNotEmpty(restapiWorkOrderApiDto.getERDAT()))
            mesPmWorkOrder.setCreateTime(DateUtils.getStrToDate("yyyyMMdd",restapiWorkOrderApiDto.getERDAT()));
        mesPmWorkOrder.setMaterialId(baseMaterial.getMaterialId());
        mesPmWorkOrder.setWorkOrderQty(new BigDecimal(restapiWorkOrderApiDto.getGAMNG()));
        pmFeignApi.updateById(mesPmWorkOrder);

        SearchMesPmWorkOrder searchMesPmWorkOrder = new SearchMesPmWorkOrder();
        searchMesPmWorkOrder.setWorkOrderCode(restapiWorkOrderApiDto.getAUFNR());
        ResponseEntity<List<MesPmWorkOrderDto>> mesPmWorkOrderList = pmFeignApi.findWorkOrderList(searchMesPmWorkOrder);
        if(StringUtils.isEmpty(mesPmWorkOrderList.getData()))   return "添加订单失败";
        MesPmWorkOrderDto mesPmWorkOrderDto = mesPmWorkOrderList.getData().get(0);

        SearchMesPmWorkOrderBom searchMesPmWorkOrderBom = new SearchMesPmWorkOrderBom();
        searchMesPmWorkOrderBom.setWorkOrderId(mesPmWorkOrderDto.getWorkOrderId());
        searchMesPmWorkOrderBom.setOption1(restapiWorkOrderApiDto.getRSPOS());
        ResponseEntity<List<MesPmWorkOrderBomDto>> mesPmWorkOrderBomList = pmFeignApi.findList(searchMesPmWorkOrderBom);
        MesPmWorkOrderBom bom = new MesPmWorkOrderBom();
        if(StringUtils.isEmpty(mesPmWorkOrderBomList.getData())){
            if(StringUtils.isEmpty(bom.getCreateTime())) bom.setCreateTime(new Date());
            bom.setOption1(restapiWorkOrderApiDto.getRSPOS());
            bom.setWorkOrderId(mesPmWorkOrderDto.getWorkOrderId());
            bom.setUsageQty(new BigDecimal(restapiWorkOrderApiDto.getMENGE()));
            pmFeignApi.addMesPmWorkOrderBom(bom);
        }else{
            bom = mesPmWorkOrderBomList.getData().get(0);
            bom.setUsageQty(new BigDecimal(restapiWorkOrderApiDto.getMENGE()));
            pmFeignApi.updateMesPmWorkOrderBom(bom);
        }
       /* for(RestapiWorkOrderBomApiDto dto : restapiWorkOrderApiDto.getWorkOrderBom()) {
            MesPmWorkOrderBom mesPmWorkOrderBom = new MesPmWorkOrderBom();
            mesPmWorkOrderBom.setUsageQty(new BigDecimal(dto.getMENGE()));
            mesPmWorkOrderBom.setWorkOrderId(mesPmWorkOrderList.getData().get(0).getWorkOrderId());
            pmFeignApi.addMesPmWorkOrderBom(mesPmWorkOrderBom);
        }*/
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


}
