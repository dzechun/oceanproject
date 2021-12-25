package com.fantechs.provider.materialapi.imes.service.impl;


import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleSetDto;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.restapi.RestapiWorkOrderApiDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseProLine;
import com.fantechs.common.base.general.entity.basic.BaseProductProcessRoute;
import com.fantechs.common.base.general.entity.basic.BaseRouteProcess;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProLine;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProductProcessRoute;
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
import java.util.*;

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
        HashSet codeSet = new HashSet();
        List<BaseOrganizationDto> orgIdList = baseUtils.getOrId();
        if(StringUtils.isEmpty(orgIdList)) return "未查询到对应的组织信息";
        Long orgId = orgIdList.get(0).getOrganizationId();
        SearchBaseBarcodeRuleSet searchBaseBarcodeRuleSet = new SearchBaseBarcodeRuleSet();
        searchBaseBarcodeRuleSet.setOrganizationId((long)1000);
        List<BaseBarcodeRuleSetDto> baseBarcodeRuleSetDtos = baseFeignApi.findBarcodeRuleSetList(searchBaseBarcodeRuleSet).getData();

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
                //产线
                List<BaseProLine> proLines = getProLine(restapiWorkOrderApiDto.getFEVOR(), orgId);
                if(StringUtils.isNotEmpty(proLines))
                    mesPmWorkOrder.setProLineId(proLines.get(0).getProLineId());
                //工艺路线
                SearchBaseProductProcessRoute searchBaseProductProcessRoute = new SearchBaseProductProcessRoute();
                searchBaseProductProcessRoute.setOrgId(orgId);
                searchBaseProductProcessRoute.setMaterialCode(baseUtils.removeZero(restapiWorkOrderApiDto.getMATNR()));
                searchBaseProductProcessRoute.setProLineId(proLines.get(0).getProLineId());
                List<BaseProductProcessRoute> productProcessRoute = baseFeignApi.findListByCondition(searchBaseProductProcessRoute).getData();
                if(StringUtils.isNotEmpty(productProcessRoute)) {
                    mesPmWorkOrder.setRouteId(productProcessRoute.get(0).getRouteId());
                    List<BaseRouteProcess> data = baseFeignApi.findConfigureRout(productProcessRoute.get(0).getRouteId()).getData();
                    if(StringUtils.isNotEmpty(data)) {
                        mesPmWorkOrder.setPutIntoProcessId(data.get(0).getProcessId());
                        mesPmWorkOrder.setOutputProcessId(data.get(data.size()-1).getProcessId());
                    }
                }else{
                    return "未配置产品工艺路线";
                }
                if(StringUtils.isNotEmpty(baseBarcodeRuleSetDtos))
                    mesPmWorkOrder.setBarcodeRuleSetId(baseBarcodeRuleSetDtos.get(0).getBarcodeRuleSetId());
                mesPmWorkOrder.setBatchCode(restapiWorkOrderApiDto.getCHARG());
                mesPmWorkOrder.setOrgId(orgId);
                mesPmWorkOrder.setIsDelete((byte)1);
                mesPmWorkOrder.setWorkOrderType((byte)0);
                mesPmWorkOrder.setWorkOrderStatus((byte)1);
                mesPmWorkOrder.setInventoryQty(BigDecimal.ZERO);
                mesPmWorkOrder.setOutputQty(BigDecimal.ZERO);
                mesPmWorkOrder.setProductionQty(BigDecimal.ZERO);
                mesPmWorkOrder.setScheduledQty(BigDecimal.ZERO);
                mesPmWorkOrder.setScrapQty(BigDecimal.ZERO);
                mesPmWorkOrder.setCreateUserId((long)99);
                mesPmWorkOrder.setCreateTime(new Date());
                ResponseEntity<MesPmWorkOrder> mesPmWorkOrderResponseEntity = pmFeignApi.saveByApi(mesPmWorkOrder);
                orderMap.put(restapiWorkOrderApiDto.getAUFNR(),mesPmWorkOrderResponseEntity.getData().getWorkOrderId());
            }

            String zMaterialCode = baseUtils.removeZero(restapiWorkOrderApiDto.getZJMATNR());
            if(StringUtils.isEmpty(bomMap.get(restapiWorkOrderApiDto.getRSPOS()))&& !codeSet.contains(zMaterialCode)){
                codeSet.add(zMaterialCode);
                MesPmWorkOrderBom bom = new MesPmWorkOrderBom();
                if (StringUtils.isNotEmpty(restapiWorkOrderApiDto.getMENGE()))
                bom.setSingleQty(new BigDecimal(restapiWorkOrderApiDto.getMENGE().trim()));
                bom.setOption1(restapiWorkOrderApiDto.getRSPOS());
                bom.setWorkOrderId(orderMap.get(restapiWorkOrderApiDto.getAUFNR()));
                List<BaseMaterial> baseMaterialss = baseUtils.getBaseMaterial(restapiWorkOrderApiDto.getZJMATNR(),orgId);
                if(StringUtils.isEmpty(baseMaterialss)) return "未查询到对应的物料编码，编码为："+restapiWorkOrderApiDto.getZJMATNR();
                bom.setPartMaterialId(baseMaterialss.get(0).getMaterialId());
                bom.setOrgId(orgId);
                bom.setIsDelete((byte)1);
                bom.setCreateUserId((long)99);
                bom.setCreateTime(new Date());
                mesPmWorkOrderBomList.add(bom);
                bomMap.put(restapiWorkOrderApiDto.getRSPOS(),restapiWorkOrderApiDto.getZJMATNR());
            }
        }
        pmFeignApi.addMesPmWorkOrderBom(mesPmWorkOrderBomList);
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

    public List<BaseProLine> getProLine(String proLineCode,Long orgId){
        SearchBaseProLine searchBaseProLine = new SearchBaseProLine();
        searchBaseProLine.setProCode(proLineCode);
        searchBaseProLine.setOrgId(orgId);
        ResponseEntity<List<BaseProLine>> list = baseFeignApi.findList(searchBaseProLine);
        return list.getData();
    }

}
