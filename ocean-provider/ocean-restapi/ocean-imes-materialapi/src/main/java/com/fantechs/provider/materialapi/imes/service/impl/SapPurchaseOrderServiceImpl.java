package com.fantechs.provider.materialapi.imes.service.impl;


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

import javax.annotation.Resource;
import javax.jws.WebService;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

@WebService(serviceName = "PurchaseOrderService", // 与接口中指定的name一致
        targetNamespace = "http://purchaseOrderService.imes.materialapi.provider.fantechs.com", // 与接口中的命名空间一致,一般是接口的包名倒
        endpointInterface = "com.fantechs.provider.materialapi.imes.service.SapPurchaseOrderService"// 接口地址
)
public class SapPurchaseOrderServiceImpl implements SapPurchaseOrderService {

    @Resource
    private OMFeignApi oMFeignApi;
    @Resource
    private BaseFeignApi baseFeignApi;

    @Override
    public String purchaseOrder(List<RestapiPurchaseOrderApiDto> purchaseOrderApiDtos) throws ParseException {

        if(StringUtils.isEmpty(purchaseOrderApiDtos)) return "采购订单参数为空";
        for(RestapiPurchaseOrderApiDto purchaseOrderApiDto : purchaseOrderApiDtos) {
            String check = check(purchaseOrderApiDto);
            if (!check.equals("1")) {
                return check;
            }
            Long orgId = getOrId();
            //保存或更新采购订单
            OmPurchaseOrder omPurchaseOrder = new OmPurchaseOrder();
            omPurchaseOrder.setPurchaseOrderCode(purchaseOrderApiDto.getEBELN());
            omPurchaseOrder.setOrderType(purchaseOrderApiDto.getBSART());
            if(StringUtils.isNotEmpty(purchaseOrderApiDto.getAEDAT()))
                omPurchaseOrder.setOrderDate(DateUtils.getStrToDate("yyyy/MM/dd", purchaseOrderApiDto.getAEDAT()));
            omPurchaseOrder.setSupplierId(getSupplier(purchaseOrderApiDto.getLIFNR(),orgId));
            omPurchaseOrder.setOrgId(orgId);
            ResponseEntity<OmPurchaseOrder> omPurchaseOrderResponseEntity = oMFeignApi.addOrUpdate(omPurchaseOrder);

            //保存或更新采购订单详情表
            OmPurchaseOrderDet purchaseOrderDet = new OmPurchaseOrderDet();
            purchaseOrderDet.setPurchaseOrderId(omPurchaseOrderResponseEntity.getData().getPurchaseOrderId());
            purchaseOrderDet.setProjectCode(purchaseOrderApiDto.getEBELP());
            purchaseOrderDet.setMaterialId(getBaseMaterial(purchaseOrderApiDto.getMATNR()).getMaterialId());
            purchaseOrderDet.setOrderQty(new BigDecimal(purchaseOrderApiDto.getMENGE().trim()));
            purchaseOrderDet.setStatus((byte)1);
            purchaseOrderDet.setWarehouseId(getWarehouse(purchaseOrderApiDto.getLGORT(),orgId));
            purchaseOrderDet.setFactoryId(getFactory(purchaseOrderApiDto.getWERKS(),orgId));
            oMFeignApi.addOrUpdate(purchaseOrderDet);
        }
        return "success";
    }


    public String check(RestapiPurchaseOrderApiDto purchaseOrderApiDto) {

        if(StringUtils.isEmpty(purchaseOrderApiDto))
            return "请求失败,参数为空";
        if(StringUtils.isEmpty(purchaseOrderApiDto.getEBELN()))
            return "请求失败,采购凭证不能为空";
        if(StringUtils.isEmpty(purchaseOrderApiDto.getMATNR()))
            return "请求失败,物料号不能为空";
        if(StringUtils.isEmpty(purchaseOrderApiDto.getEBELP()))
            return "请求失败,采购凭证项目号不能为空";
        if(StringUtils.isEmpty(purchaseOrderApiDto.getMENGE()))
            return "请求失败,采购数量不能为空";
        return "1";
    }

    public Long getSupplier(String supplierCode,Long orgId){
        SearchBaseSupplier searchBaseSupplier = new SearchBaseSupplier();
        searchBaseSupplier.setSupplierCode(supplierCode);
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
        searchBaseWarehouse.setWarehouseCode(warehouseCode);
        searchBaseWarehouse.setOrgId(orgId);
        ResponseEntity<List<BaseWarehouse>> baseWarehouseList = baseFeignApi.findList(searchBaseWarehouse);
        if(StringUtils.isEmpty(baseWarehouseList.getData()))
            throw new BizErrorException("未查询到对应的供应商，编码为："+warehouseCode);
        return baseWarehouseList.getData().get(0).getWarehouseId();
    }

    /**
     * 获取组织id方法
     *
     * @return
     */
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

    public Long getFactory(String factoryCode,Long orgId){
        SearchBaseFactory searchBaseFactory = new SearchBaseFactory();
        searchBaseFactory.setFactoryCode(factoryCode);
        searchBaseFactory.setOrgId(orgId);
        ResponseEntity<List<BaseFactoryDto>> factoryList = baseFeignApi.findFactoryList(searchBaseFactory);
        if(StringUtils.isEmpty(factoryList.getData()))
            throw new BizErrorException("未查询到对应的工厂，编码为："+factoryCode);
        return factoryList.getData().get(0).getFactoryId();
    }
}
