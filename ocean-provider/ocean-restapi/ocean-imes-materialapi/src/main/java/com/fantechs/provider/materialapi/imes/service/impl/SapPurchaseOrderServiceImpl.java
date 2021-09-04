package com.fantechs.provider.materialapi.imes.service.impl;


import com.codingapi.txlcn.tc.annotation.LcnTransaction;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.basic.BaseFactoryDto;
import com.fantechs.common.base.general.dto.basic.BaseOrganizationDto;
import com.fantechs.common.base.general.dto.restapi.RestapiPurchaseOrderApiDto;
import com.fantechs.common.base.general.entity.basic.BaseMaterial;
import com.fantechs.common.base.general.entity.basic.BaseSupplier;
import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.general.entity.basic.search.*;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.jws.WebService;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @LcnTransaction
    public String purchaseOrder(List<RestapiPurchaseOrderApiDto> purchaseOrderApiDtos) throws ParseException {

        if(StringUtils.isEmpty(purchaseOrderApiDtos)) return "采购订单参数为空";
        Map<String,Long> purchaseMap = new HashMap<String,Long>();
        List<OmPurchaseOrderDet> omPurchaseOrderDetList = new ArrayList<OmPurchaseOrderDet>();
        Long orgId =baseUtils.getOrId();
        for(RestapiPurchaseOrderApiDto purchaseOrderApiDto : purchaseOrderApiDtos) {
            String check = check(purchaseOrderApiDto);
            if (!check.equals("1")) {
                logsUtils.addlog((byte)0,(byte)2,(long)1002,check,purchaseOrderApiDto.toString());
                return check;
            }
            //保存或更新采购订单
            if(StringUtils.isEmpty(purchaseMap.get(purchaseOrderApiDto.getEBELN()))) {
                OmPurchaseOrder omPurchaseOrder = new OmPurchaseOrder();
                omPurchaseOrder.setPurchaseOrderCode(baseUtils.removeZero(purchaseOrderApiDto.getEBELN()));
                omPurchaseOrder.setOrderType(purchaseOrderApiDto.getBSART());
                if (StringUtils.isNotEmpty(purchaseOrderApiDto.getAEDAT()))
                    omPurchaseOrder.setOrderDate(DateUtils.getStrToDate("yyyyMMdd", purchaseOrderApiDto.getAEDAT()));
                omPurchaseOrder.setSupplierId(getSupplier(purchaseOrderApiDto.getLIFNR(), orgId));
                omPurchaseOrder.setOrgId(orgId);
                omPurchaseOrder.setItemCategoryName(purchaseOrderApiDto.getEPSTP());
                omPurchaseOrder.setOrderUnitName(purchaseOrderApiDto.getMEINS());
                omPurchaseOrder.setInventorySite(purchaseOrderApiDto.getLGORT());
                omPurchaseOrder.setFreeItem(purchaseOrderApiDto.getUMSON());
                omPurchaseOrder.setSalesReturnItem(purchaseOrderApiDto.getRETPO());

                ResponseEntity<OmPurchaseOrder> omPurchaseOrderResponseEntity = oMFeignApi.saveByApi(omPurchaseOrder);
                purchaseMap.put(purchaseOrderApiDto.getEBELN(),omPurchaseOrderResponseEntity.getData().getPurchaseOrderId());
            }
            //保存或更新采购订单详情表
            OmPurchaseOrderDet purchaseOrderDet = new OmPurchaseOrderDet();
            purchaseOrderDet.setPurchaseOrderId(purchaseMap.get(purchaseOrderApiDto.getEBELN()));
            purchaseOrderDet.setProjectCode(baseUtils.removeZero(purchaseOrderApiDto.getEBELP()));
            purchaseOrderDet.setMaterialId(baseUtils.getBaseMaterial(purchaseOrderApiDto.getMATNR()).getMaterialId());
            purchaseOrderDet.setOrderQty(new BigDecimal(purchaseOrderApiDto.getMENGE().trim()));
            purchaseOrderDet.setStatus((byte)1);
            purchaseOrderDet.setWarehouseId(getWarehouse(purchaseOrderApiDto.getLGORT(),orgId));
            purchaseOrderDet.setFactoryId(getFactory(purchaseOrderApiDto.getWERKS(),orgId));
            purchaseOrderDet.setIsDelete((byte)1);
            omPurchaseOrderDetList.add(purchaseOrderDet);
        }
        oMFeignApi.saveByApi(omPurchaseOrderDetList);
        logsUtils.addlog((byte)0,(byte)2,orgId,null,null);
        return "success";
    }


    public String check(RestapiPurchaseOrderApiDto purchaseOrderApiDto) {
        String check = "1";
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

    public Long getSupplier(String supplierCode,Long orgId){
        SearchBaseSupplier searchBaseSupplier = new SearchBaseSupplier();
        searchBaseSupplier.setSupplierCode(baseUtils.removeZero(supplierCode));
        searchBaseSupplier.setOrganizationId(orgId);
        ResponseEntity<List<BaseSupplier>> baseSupplierList = baseFeignApi.findSupplierList(searchBaseSupplier);
        if(StringUtils.isEmpty(baseSupplierList.getData()))
            throw new BizErrorException("未查询到对应的供应商，编码为："+supplierCode);
        return baseSupplierList.getData().get(0).getSupplierId();
    }

    /**
     * 获取工厂id方法
     *
     * @return
     */
    public Long getWarehouse(String warehouseCode,Long orgId){
        SearchBaseWarehouse searchBaseWarehouse = new SearchBaseWarehouse();
        searchBaseWarehouse.setWarehouseCode(baseUtils.removeZero(warehouseCode));
        searchBaseWarehouse.setOrgId(orgId);
        ResponseEntity<List<BaseWarehouse>> baseWarehouseList = baseFeignApi.findList(searchBaseWarehouse);
        if(StringUtils.isEmpty(baseWarehouseList.getData()))
            throw new BizErrorException("未查询到对应的供应商，编码为："+warehouseCode);
        return baseWarehouseList.getData().get(0).getWarehouseId();
    }

    public Long getFactory(String factoryCode,Long orgId){
        SearchBaseFactory searchBaseFactory = new SearchBaseFactory();
        searchBaseFactory.setFactoryCode(baseUtils.removeZero(factoryCode));
        searchBaseFactory.setOrgId(orgId);
        ResponseEntity<List<BaseFactoryDto>> factoryList = baseFeignApi.findFactoryList(searchBaseFactory);
        if(StringUtils.isEmpty(factoryList.getData()))
            throw new BizErrorException("未查询到对应的工厂，编码为："+factoryCode);
        return factoryList.getData().get(0).getFactoryId();
    }
}
