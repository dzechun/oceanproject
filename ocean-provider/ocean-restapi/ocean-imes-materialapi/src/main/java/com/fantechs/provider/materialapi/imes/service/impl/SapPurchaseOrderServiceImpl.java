package com.fantechs.provider.materialapi.imes.service.impl;



import com.fantechs.common.base.general.dto.basic.BaseFactoryDto;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.restapi.RestapiPurchaseOrderApiDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseSupplier;
import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseFactory;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseSupplier;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseWarehouse;
import com.fantechs.common.base.general.entity.om.OmPurchaseOrder;
import com.fantechs.common.base.general.entity.om.OmPurchaseOrderDet;
import com.fantechs.common.base.response.ResponseEntity;
import com.fantechs.common.base.utils.DateUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.qms.OMFeignApi;
import com.fantechs.provider.materialapi.imes.service.SapPurchaseOrderService;
import com.fantechs.provider.materialapi.imes.utils.BaseUtils;
import com.fantechs.provider.materialapi.imes.utils.LogsUtils;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.jws.WebService;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

@WebService(serviceName = "PurchaseOrderService", // 与接口中指定的name一致
        targetNamespace = "http://purchaseOrderService.imes.materialapi.provider.fantechs.com", // 与接口中的命名空间一致,一般是接口的包名倒
        endpointInterface = "com.fantechs.provider.materialapi.imes.service.SapPurchaseOrderService"// 接口地址
)
public class SapPurchaseOrderServiceImpl implements SapPurchaseOrderService {
    private static final Logger logger = LoggerFactory.getLogger(SapPurchaseOrderServiceImpl.class);

    @Resource
    private OMFeignApi oMFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;
    @Resource
    private BaseUtils baseUtils;
    @Resource
    private LogsUtils logsUtils;


    @Override
    @GlobalTransactional
    public String purchaseOrder(List<RestapiPurchaseOrderApiDto> purchaseOrderApiDtos) {

        if(StringUtils.isEmpty(purchaseOrderApiDtos)) return "采购订单参数为空";
        Map<String,Long> purchaseMap = new HashMap<String,Long>();
        List<OmPurchaseOrderDet> omPurchaseOrderDetList = new ArrayList<OmPurchaseOrderDet>();

        List<BaseOrganizationDto> orgIdList = baseUtils.getOrId();
        if(StringUtils.isEmpty(orgIdList)) return "未查询到对应的组织信息";
        Long orgId = orgIdList.get(0).getOrganizationId();

        for(RestapiPurchaseOrderApiDto purchaseOrderApiDto : purchaseOrderApiDtos) {
            String check = "1";
            check = check(purchaseOrderApiDto,check);
            if (!check.equals("1")) {
       //         logsUtils.addlog((byte)0,(byte)2,(long)1002,check,purchaseOrderApiDto.toString());
                return check;
            }

            SearchBaseSupplier searchBaseSupplier = new SearchBaseSupplier();
            searchBaseSupplier.setSupplierCode(baseUtils.removeZero(purchaseOrderApiDto.getLIFNR()));
            searchBaseSupplier.setOrganizationId(orgId);
            ResponseEntity<List<BaseSupplier>> baseSupplierList = baseFeignApi.findSupplierList(searchBaseSupplier);
            if(StringUtils.isEmpty(baseSupplierList.getData()))
                return "未查询到对应的供应商，编码为："+purchaseOrderApiDto.getLIFNR();

            SearchBaseWarehouse searchBaseWarehouse = new SearchBaseWarehouse();
            searchBaseWarehouse.setWarehouseCode(baseUtils.removeZero(purchaseOrderApiDto.getLGORT()));
            searchBaseWarehouse.setOrgId(orgId);
            ResponseEntity<List<BaseWarehouse>> baseWarehouseList = baseFeignApi.findList(searchBaseWarehouse);
            if(StringUtils.isEmpty(baseWarehouseList.getData()))
                return"未查询到对应的仓库，编码为："+purchaseOrderApiDto.getLGORT();

            SearchBaseFactory searchBaseFactory = new SearchBaseFactory();
            searchBaseFactory.setFactoryCode(baseUtils.removeZero(purchaseOrderApiDto.getWERKS()));
            searchBaseFactory.setOrgId(orgId);
            ResponseEntity<List<BaseFactoryDto>> factoryList = baseFeignApi.findFactoryList(searchBaseFactory);
            if(StringUtils.isEmpty(factoryList.getData()))
                return "未查询到对应的工厂，编码为："+purchaseOrderApiDto.getWERKS();

            //保存或更新采购订单
            if(StringUtils.isEmpty(purchaseMap.get(purchaseOrderApiDto.getEBELN()))) {
                OmPurchaseOrder omPurchaseOrder = new OmPurchaseOrder();
                omPurchaseOrder.setPurchaseOrderCode(baseUtils.removeZero(purchaseOrderApiDto.getEBELN()));
                omPurchaseOrder.setOrderType(purchaseOrderApiDto.getBSART());
                if (StringUtils.isNotEmpty(purchaseOrderApiDto.getAEDAT())) {
                    try {
                        omPurchaseOrder.setOrderDate(DateUtils.getStrToDate("yyyyMMdd", purchaseOrderApiDto.getAEDAT()));
                    } catch (ParseException e) {
                        return "时间格式错误";
                    }
                }
                omPurchaseOrder.setSupplierId(baseSupplierList.getData().get(0).getSupplierId());
                omPurchaseOrder.setOrgId(orgId);
                omPurchaseOrder.setItemCategoryName(purchaseOrderApiDto.getEPSTP());
                omPurchaseOrder.setOrderUnitName(purchaseOrderApiDto.getMEINS());
                omPurchaseOrder.setInventorySite(purchaseOrderApiDto.getLGORT());
                omPurchaseOrder.setFreeItem(purchaseOrderApiDto.getUMSON());
                omPurchaseOrder.setSalesReturnItem(purchaseOrderApiDto.getRETPO());
                omPurchaseOrder.setOrderStatus((byte)1);
                omPurchaseOrder.setStatus((byte)1);
                omPurchaseOrder.setCreateUserId((long)99);
                omPurchaseOrder.setCreateTime(new Date());
                ResponseEntity<OmPurchaseOrder> omPurchaseOrderResponseEntity = oMFeignApi.saveByApi(omPurchaseOrder);
                purchaseMap.put(purchaseOrderApiDto.getEBELN(),omPurchaseOrderResponseEntity.getData().getPurchaseOrderId());
            }
            //保存或更新采购订单详情表
            OmPurchaseOrderDet purchaseOrderDet = new OmPurchaseOrderDet();
            purchaseOrderDet.setPurchaseOrderId(purchaseMap.get(purchaseOrderApiDto.getEBELN()));
            purchaseOrderDet.setProjectCode(baseUtils.removeZero(purchaseOrderApiDto.getEBELP()));
            List<BaseMaterial> baseMaterials = baseUtils.getBaseMaterial(purchaseOrderApiDto.getMATNR(),orgId);
            if(StringUtils.isEmpty(baseMaterials)) return "未查询到对应的物料编码，编码为："+purchaseOrderApiDto.getMATNR();


            purchaseOrderDet.setMaterialId(baseMaterials.get(0).getMaterialId());
            purchaseOrderDet.setOrderQty(new BigDecimal(purchaseOrderApiDto.getMENGE().trim()));
            purchaseOrderDet.setStatus((byte)1);
//            purchaseOrderDet.setWarehouseId(baseWarehouseList.getData().get(0).getWarehouseId());
            purchaseOrderDet.setFactoryId(factoryList.getData().get(0).getFactoryId());
            purchaseOrderDet.setIsDelete((byte)1);
            purchaseOrderDet.setOrgId(orgId);
            purchaseOrderDet.setCreateUserId((long)99);
            purchaseOrderDet.setCreateTime(new Date());
            omPurchaseOrderDetList.add(purchaseOrderDet);
        }
        oMFeignApi.saveByApi(omPurchaseOrderDetList);
        //logsUtils.addlog((byte)0,(byte)2,orgId,null,null);
        return "success";
    }


    public String check(RestapiPurchaseOrderApiDto purchaseOrderApiDto,String check) {

        if(StringUtils.isEmpty(purchaseOrderApiDto))
            check = "请求失败,参数为空";
        if(StringUtils.isEmpty(purchaseOrderApiDto.getEBELN()))
            check = "请求失败,采购凭证不能为空";
        if(StringUtils.isEmpty(purchaseOrderApiDto.getMATNR()))
            check = "请求失败,物料号不能为空";
        if(StringUtils.isEmpty(purchaseOrderApiDto.getEBELP()))
            check = "请求失败,采购凭证项目号不能为空";
        if(StringUtils.isEmpty(purchaseOrderApiDto.getMENGE()))
            check = "请求失败,采购数量不能为空";
        return check;
    }

}
