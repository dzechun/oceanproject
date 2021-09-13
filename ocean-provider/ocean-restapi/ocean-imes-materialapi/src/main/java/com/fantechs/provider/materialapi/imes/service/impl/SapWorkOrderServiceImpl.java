package com.fantechs.provider.materialapi.imes.service.impl;


import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.restapi.RestapiWorkOrderApiDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrderBom;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.DateUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.materialapi.imes.service.SapWorkOrderService;
import com.fantechs.provider.materialapi.imes.utils.BaseUtils;
import com.fantechs.provider.materialapi.imes.utils.LogsUtils;

import javax.annotation.Resource;
import javax.jws.WebService;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebService(serviceName = "WorkOrderService", // 与接口中指定的name一致
        targetNamespace = "http://workOrder.imes.materialapi.provider.fantechs.com", // 与接口中的命名空间一致,一般是接口的包名倒
        endpointInterface = "com.fantechs.provider.materialapi.imes.service.SapWorkOrderService"// 接口地址
)
public class SapWorkOrderServiceImpl implements SapWorkOrderService {

    @Resource
    private PMFeignApi pmFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private LogsUtils logsUtils;
    @Resource
    private BaseUtils baseUtils;

    @Override
    @LcnTransaction
    public String saveWorkOrder(List<RestapiWorkOrderApiDto> restapiWorkOrderApiDtos) throws ParseException {
        if(StringUtils.isEmpty(restapiWorkOrderApiDtos)) return "工单参数为空";
        Map<String,Long> orderMap = new HashMap<String,Long>();
        Map<String,String> bomMap = new HashMap<String,String>();
        List<MesPmWorkOrderBom> mesPmWorkOrderBomList = new ArrayList<MesPmWorkOrderBom>();

        List<BaseOrganizationDto> orgIdList = baseUtils.getOrId();
        if(StringUtils.isEmpty(orgIdList)) return "未查询到对应的组织信息";
        Long orgId = orgIdList.get(0).getOrganizationId();

        for(RestapiWorkOrderApiDto restapiWorkOrderApiDto : restapiWorkOrderApiDtos) {
            String check = check(restapiWorkOrderApiDto);
            if (!check.equals("1")) {
                logsUtils.addlog((byte)0,(byte)2,orgId,check,restapiWorkOrderApiDto.toString());
                return check;
            }
            //保存工单，如已经保存则后续循环不再保存
            if(StringUtils.isEmpty(orderMap.get(restapiWorkOrderApiDto.getAUFNR()))){
                MesPmWorkOrder mesPmWorkOrder = new MesPmWorkOrder();
                mesPmWorkOrder.setWorkOrderCode(baseUtils.removeZero(restapiWorkOrderApiDto.getAUFNR()));
                if (StringUtils.isNotEmpty(restapiWorkOrderApiDto.getGSTRP()))
                    mesPmWorkOrder.setPlanStartTime(DateUtils.getStrToDate("yyyyMMdd", restapiWorkOrderApiDto.getGSTRP()));
                if (StringUtils.isNotEmpty(restapiWorkOrderApiDto.getGLTRP()))
                    mesPmWorkOrder.setPlanEndTime(DateUtils.getStrToDate("yyyyMMdd", restapiWorkOrderApiDto.getGLTRP()));
                List<BaseMaterial> baseMaterials = baseUtils.getBaseMaterial(restapiWorkOrderApiDto.getMATNR(),orgId);
                if(StringUtils.isEmpty(baseMaterials)) return "未查询到对应的物料编码，编码为："+restapiWorkOrderApiDto.getMATNR();
                mesPmWorkOrder.setMaterialId(baseMaterials.get(0).getMaterialId());
                if(StringUtils.isNotEmpty(restapiWorkOrderApiDto.getGAMNG()))
                 mesPmWorkOrder.setWorkOrderQty(new BigDecimal(restapiWorkOrderApiDto.getGAMNG().trim()));
                //目前还未同步产线
                //mesPmWorkOrder.setProLineId(getProLine(restapiWorkOrderApiDto.getFEVOR()));
                mesPmWorkOrder.setOrgId(orgId);
                mesPmWorkOrder.setIsDelete((byte)1);
                ResponseEntity<MesPmWorkOrder> mesPmWorkOrderResponseEntity = pmFeignApi.saveByApi(mesPmWorkOrder);
                orderMap.put(restapiWorkOrderApiDto.getAUFNR(),mesPmWorkOrderResponseEntity.getData().getWorkOrderId());
            }


            if(StringUtils.isEmpty(bomMap.get(restapiWorkOrderApiDto.getRSPOS()))){

                MesPmWorkOrderBom bom = new MesPmWorkOrderBom();
                if (StringUtils.isNotEmpty(restapiWorkOrderApiDto.getMENGE()))
                bom.setUsageQty(new BigDecimal(restapiWorkOrderApiDto.getMENGE().trim()));
                bom.setOption1(restapiWorkOrderApiDto.getRSPOS());
                bom.setWorkOrderId(orderMap.get(restapiWorkOrderApiDto.getAUFNR()));
                List<BaseMaterial> baseMaterialss = baseUtils.getBaseMaterial(restapiWorkOrderApiDto.getZJMATNR(),orgId);
                if(StringUtils.isEmpty(baseMaterialss)) return "未查询到对应的物料编码，编码为："+restapiWorkOrderApiDto.getZJMATNR();
                bom.setPartMaterialId(baseMaterialss.get(0).getMaterialId());
                bom.setOrgId(orgId);
                bom.setIsDelete((byte)1);
                mesPmWorkOrderBomList.add(bom);
                bomMap.put(restapiWorkOrderApiDto.getRSPOS(),restapiWorkOrderApiDto.getZJMATNR());
            }
            pmFeignApi.addMesPmWorkOrderBom(mesPmWorkOrderBomList);
        }
        logsUtils.addlog((byte)1,(byte)2,orgId,null,null);
        return "success";
    }


    public String check(RestapiWorkOrderApiDto restapiWorkOrderApiDto) {
        String check = "1";
        if(StringUtils.isEmpty(restapiWorkOrderApiDto))
            check = "请求失败,参数为空";
        if(StringUtils.isEmpty(restapiWorkOrderApiDto.getAUFNR()))
            check = "请求失败,工单号不能为空";
        if(StringUtils.isEmpty(restapiWorkOrderApiDto.getMATNR()))
            check = "请求失败,物料号不能为空";
        return check;
    }

    /*public Long getProLine(String proLineCode,Long orgId){
        SearchBaseProLine searchBaseProLine = new SearchBaseProLine();
        searchBaseProLine.setProCode(proLineCode);
        searchBaseProLine.setOrgId(orgId);
        ResponseEntity<List<BaseProLine>> list = baseFeignApi.findList(searchBaseProLine);
        if(StringUtils.isEmpty(list.getData()))
            throw new BizErrorException("未查询到对应的产线："+proLineCode);
        return list.getData().get(0).getProLineId();
    }*/

}
