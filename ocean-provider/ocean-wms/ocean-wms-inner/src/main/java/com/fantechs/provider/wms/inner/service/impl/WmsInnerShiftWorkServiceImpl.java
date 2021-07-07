package com.fantechs.provider.wms.inner.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.*;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.basic.BaseWarehouse;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseStorage;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcode;
import com.fantechs.common.base.general.entity.wms.inner.*;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrderDet;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.wms.inner.mapper.WmsInnerJobOrderDetMapper;
import com.fantechs.provider.wms.inner.mapper.WmsInnerJobOrderMapper;
import com.fantechs.provider.wms.inner.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Service
public class WmsInnerShiftWorkServiceImpl implements WmsInnerShiftWorkService {

    @Resource
    WmsInnerJobOrderService wmsInnerJobOrderService;

    @Resource
    WmsInnerHtJobOrderService wmsInnerHtJobOrderService;

    @Resource
    WmsInnerJobOrderMapper wmsInnerJobOrderMapper;

    @Resource
    WmsInnerJobOrderDetService wmsInnerJobOrderDetService;

    @Resource
    WmsInnerHtJobOrderDetService wmsInnerHtJobOrderDetService;

    @Resource
    WmsInnerJobOrderDetMapper wmsInnerJobOrderDetMapper;

    @Resource
    WmsInnerJobOrderDetBarcodeService wmsInnerJobOrderDetBarcodeService;

    @Resource
    WmsInnerHtJobOrderDetBarcodeService wmsInnerHtJobOrderDetBarcodeService;

    @Resource
    WmsInnerInventoryDetService wmsInnerInventoryDetService;

    @Resource
    WmsInnerInventoryService wmsInnerInventoryService;

    @Resource
    BaseFeignApi baseFeignApi;

    @Resource
    SFCFeignApi sfcFeignApi;

    @Resource
    PMFeignApi pmFeignApi;

    @Override
    public List<WmsInnerJobOrderDto> pdaFindList(Map<String, Object> map) {
        SysUser sysUser = currentUser();
        map.put("orgId", sysUser.getOrganizationId());
        map.put("jobOrderType", (byte) 2);
        return wmsInnerJobOrderService.findShiftList(map);
    }

    @Override
    public List<WmsInnerJobOrderDto> pdaFindShiftList(Map<String, Object> map) {
        SysUser sysUser = currentUser();
        map.put("orgId", sysUser.getOrganizationId());
        map.put("jobOrderType", (byte) 2);
        if (StringUtils.isNotEmpty(map.get("jobOrderCode"))) {
            map.put("jobOrderCode", map.get("jobOrderCode"));
        }
        return wmsInnerJobOrderService.pdaFindShiftList(map);
    }

    @Override
    public List<WmsInnerJobOrderDetDto> pdaFindDetList(SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet) {
        searchWmsInnerJobOrderDet.setNonShiftStorageStatus((byte) 4);
        return wmsInnerJobOrderDetService.findList(searchWmsInnerJobOrderDet);
    }

    @Override
    @Transactional
    public String saveShiftWorkDetBarcode(SaveShiftWorkDetDto dto) {
        SysUser sysUser = currentUser();

        if (dto.getBarcodes() == null && dto.getBarcodes().size() <= 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA5001006);
        }
        // 判断是否有作业单
        if (!dto.getIsPda()) {
            // 移位作业明细单 变更移位状态
            WmsInnerJobOrderDet jobOrderDet = wmsInnerJobOrderDetService.selectByKey(dto.getJobOrderDetId());
            jobOrderDet.setShiftStorageStatus((byte) 2);
            wmsInnerJobOrderDetService.update(jobOrderDet);

            // 作业单拣货数量+1以及变更单据状态
            WmsInnerJobOrder innerJobOrder = wmsInnerJobOrderService.selectByKey(dto.getJobOrderId());
            innerJobOrder.setOrderStatus((byte) 4);
            innerJobOrder.setActualQty(innerJobOrder.getActualQty() != null ? innerJobOrder.getActualQty().add(new BigDecimal(dto.getMaterialQty())) : BigDecimal.ONE);
            wmsInnerJobOrderService.update(innerJobOrder);
        } else {
            // 查询库存信息，同一库位跟同物料有且只有一条数据
            Map<String, Object> map = new HashMap<>();
            map.put("materialId", dto.getMaterialId());
            map.put("storageId", dto.getStorageId());
            map.put("jobStatus", (byte) 2);
            map.put("lockStatus", (byte) 0);
            map.put("stockLock", (byte) 0);
            map.put("qcLock", (byte) 0);
            List<WmsInnerInventoryDto> innerInventoryDtos = wmsInnerInventoryService.findList(map);
            if (innerInventoryDtos == null || innerInventoryDtos.size() <= 0){
                throw new BizErrorException(ErrorCodeEnum.PDA5001009);
            }
            WmsInnerInventoryDto innerInventoryDto = innerInventoryDtos.get(0);
            if (innerInventoryDto.getPackingQty().compareTo(new BigDecimal(dto.getMaterialQty())) < -1){
                throw new BizErrorException(ErrorCodeEnum.PDA5001012);
            }

            if(dto.getJobOrderId() != null){
                // 更新移位单
                WmsInnerJobOrder innerJobOrder = wmsInnerJobOrderService.selectByKey(dto.getJobOrderId());
                innerJobOrder.setOrderStatus((byte) 4);
                innerJobOrder.setActualQty(innerJobOrder.getActualQty() != null ? innerJobOrder.getActualQty().add(new BigDecimal(dto.getMaterialQty())) : BigDecimal.ONE);
                innerJobOrder.setPlanQty(innerJobOrder.getActualQty());
                wmsInnerJobOrderService.update(innerJobOrder);
            }else {
                // 创建移位单
                WmsInnerJobOrder innerJobOrder = new WmsInnerJobOrder();
                innerJobOrder.setWarehouseId(dto.getWarehouseId());
                innerJobOrder.setWorkerId(sysUser.getUserId());
                innerJobOrder.setJobOrderCode(CodeUtils.getId("SHIFT-"));
                innerJobOrder.setJobOrderType((byte) 2);
                innerJobOrder.setPlanQty(new BigDecimal(dto.getMaterialQty()));
                innerJobOrder.setActualQty(innerJobOrder.getPlanQty());
                innerJobOrder.setOrderStatus((byte) 4);
                innerJobOrder.setStatus((byte) 1);
                innerJobOrder.setOrgId(sysUser.getOrganizationId());
                innerJobOrder.setCreateTime(new Date());
                innerJobOrder.setCreateUserId(sysUser.getUserId());
                innerJobOrder.setIsDelete((byte) 1);
                wmsInnerJobOrderMapper.insertUseGeneratedKeys(innerJobOrder);
                dto.setJobOrderId(innerJobOrder.getJobOrderId());
                WmsInnerHtJobOrder wmsInnerHtJobOrder = new WmsInnerHtJobOrder();
                BeanUtil.copyProperties(innerJobOrder, wmsInnerHtJobOrder);
                wmsInnerHtJobOrderService.save(wmsInnerHtJobOrder);
            }

            // 创建移位明细
            WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
            wmsInnerJobOrderDet.setJobOrderId(dto.getJobOrderId());
            wmsInnerJobOrderDet.setMaterialOwnerId(innerInventoryDto.getMaterialOwnerId());
            wmsInnerJobOrderDet.setWarehouseId(dto.getWarehouseId());
            wmsInnerJobOrderDet.setOutStorageId(dto.getStorageId());
            wmsInnerJobOrderDet.setMaterialId(innerInventoryDto.getMaterialId());
            wmsInnerJobOrderDet.setPackingUnitName(innerInventoryDto.getPackingUnitName());
            wmsInnerJobOrderDet.setPlanQty(new BigDecimal(dto.getMaterialQty()));
            wmsInnerJobOrderDet.setDistributionQty(wmsInnerJobOrderDet.getPlanQty());
            wmsInnerJobOrderDet.setActualQty(wmsInnerJobOrderDet.getPlanQty());
            wmsInnerJobOrderDet.setPalletCode(innerInventoryDto.getPalletCode());
            wmsInnerJobOrderDet.setReceivingDate(innerInventoryDto.getReceivingDate());
            wmsInnerJobOrderDet.setProductionDate(innerInventoryDto.getProductionDate());
            wmsInnerJobOrderDet.setInventoryStatusId(innerInventoryDto.getInventoryStatusId());
            wmsInnerJobOrderDet.setExpiredDate(innerInventoryDto.getExpiredDate());
            wmsInnerJobOrderDet.setWorkStartTime(new Date());
            wmsInnerJobOrderDet.setWorkEndTime(new Date());
            wmsInnerJobOrderDet.setBatchCode(innerInventoryDto.getBatchCode());
            wmsInnerJobOrderDet.setStatus((byte) 1);
            wmsInnerJobOrderDet.setRemark(innerInventoryDto.getRemark());
            wmsInnerJobOrderDet.setOrgId(sysUser.getOrganizationId());
            wmsInnerJobOrderDet.setCreateTime(new Date());
            wmsInnerJobOrderDet.setCreateUserId(sysUser.getUserId());
            wmsInnerJobOrderDet.setIsDelete((byte) 1);
            wmsInnerJobOrderDet.setShiftStorageStatus((byte) 3);
            wmsInnerJobOrderDetMapper.insertUseGeneratedKeys(wmsInnerJobOrderDet);
            WmsInnerHtJobOrderDet innerHtJobOrderDet = new WmsInnerHtJobOrderDet();
            BeanUtil.copyProperties(wmsInnerJobOrderDet, innerHtJobOrderDet);
            wmsInnerHtJobOrderDetService.save(innerHtJobOrderDet);

            // 新增待出库存信息
            WmsInnerInventory newInnerInventory = new WmsInnerInventory();
            BeanUtil.copyProperties(innerInventoryDto, newInnerInventory);
            newInnerInventory.setJobStatus((byte) 3);
            newInnerInventory.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
            newInnerInventory.setOrgId(sysUser.getOrganizationId());
            newInnerInventory.setCreateTime(new Date());
            newInnerInventory.setCreateUserId(sysUser.getUserId());
            wmsInnerInventoryService.save(newInnerInventory);
            // 变更减少原库存
            innerInventoryDto.setPackingQty(innerInventoryDto.getPackingQty().subtract(new BigDecimal(dto.getMaterialQty())));
            wmsInnerInventoryService.update(innerInventoryDto);

        }

        List<WmsInnerJobOrderDetBarcode> jobOrderDetBarcodeList = new ArrayList<>();
        List<WmsInnerHtJobOrderDetBarcode> htJobOrderDetBarcodes = new ArrayList<>();
        for (String barcode : dto.getBarcodes()) {
            // 查询条码
            MesSfcWorkOrderBarcode workOrderBarcode = sfcFeignApi.findBarcode(barcode).getData();
            if (workOrderBarcode == null) {
                throw new BizErrorException(ErrorCodeEnum.PDA40012000);
            }
            // 创建条码移位单明细关系
            WmsInnerJobOrderDetBarcode wmsInnerJobOrderDetBarcode = new WmsInnerJobOrderDetBarcode();
            wmsInnerJobOrderDetBarcode.setBarcode(barcode);
            wmsInnerJobOrderDetBarcode.setJobOrderDetId(dto.getJobOrderDetId());
            wmsInnerJobOrderDetBarcode.setWorkOrderBarcodeId(workOrderBarcode.getWorkOrderBarcodeId());
            wmsInnerJobOrderDetBarcode.setStatus((byte) 1);
            wmsInnerJobOrderDetBarcode.setOrgId(sysUser.getOrganizationId());
            wmsInnerJobOrderDetBarcode.setCreateTime(new Date());
            wmsInnerJobOrderDetBarcode.setCreateUserId(sysUser.getUserId());
            wmsInnerJobOrderDetBarcode.setIsDelete((byte) 1);
            jobOrderDetBarcodeList.add(wmsInnerJobOrderDetBarcode);
            WmsInnerHtJobOrderDetBarcode innerHtJobOrderDetBarcode = new WmsInnerHtJobOrderDetBarcode();
            BeanUtil.copyProperties(wmsInnerJobOrderDetBarcode, innerHtJobOrderDetBarcode);
            htJobOrderDetBarcodes.add(innerHtJobOrderDetBarcode);
        }

        if (jobOrderDetBarcodeList.size() > 0) {
            wmsInnerJobOrderDetBarcodeService.batchSave(jobOrderDetBarcodeList);
        }
        if (htJobOrderDetBarcodes.size() > 0) {
            wmsInnerHtJobOrderDetBarcodeService.batchSave(htJobOrderDetBarcodes);
        }

        return dto.getJobOrderId().toString();
    }

    @Override
    public CheckShiftWorkBarcodeRecordDto checkShiftWorkBarcode(CheckShiftWorkBarcodeDto dto) {
        if (StringUtils.isEmpty(dto.getBarcode())) {
            throw new BizErrorException(ErrorCodeEnum.PDA5001001);
        }
        if (StringUtils.isEmpty(dto.getStorageId())) {
            throw new BizErrorException(ErrorCodeEnum.PDA5001003);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("storageId", dto.getStorageId());
        map.put("barcode", dto.getBarcode());
        List<WmsInnerInventoryDetDto> inventoryDetDtos = wmsInnerInventoryDetService.findList(map);
        if (inventoryDetDtos == null || inventoryDetDtos.size() <= 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA5001004);
        }
        WmsInnerInventoryDetDto inventoryDetDto = inventoryDetDtos.get(0);
        if (dto.getWarehouseId() != null && !inventoryDetDto.getWarehouseId().equals(dto.getWarehouseId())) {
            throw new BizErrorException(ErrorCodeEnum.PDA5001005);
        }
        if (!inventoryDetDto.getStorageId().equals(dto.getStorageId())) {
            throw new BizErrorException(ErrorCodeEnum.PDA5001000);
        }
        BaseStorage baseStorage = baseFeignApi.detail(inventoryDetDto.getStorageId()).getData();
        if (baseStorage == null) {
            throw new BizErrorException(ErrorCodeEnum.PDA5001007);
        }
        if(baseStorage.getStorageType() != (byte)1){
            throw new BizErrorException(ErrorCodeEnum.PDA5001008);
        }
        // 查询库存信息，同一库位跟同物料有且只有一条数据
        map.clear();
        map.put("materialId", inventoryDetDto.getMaterialId());
        map.put("storageId", dto.getStorageId());
        map.put("jobStatus", (byte) 2);
        map.put("lockStatus", (byte) 0);
        map.put("stockLock", (byte) 0);
        map.put("qcLock", (byte) 0);
        List<WmsInnerInventoryDto> innerInventoryDtos = wmsInnerInventoryService.findList(map);
        if (innerInventoryDtos == null || innerInventoryDtos.size() <= 0){
            throw new BizErrorException(ErrorCodeEnum.PDA5001009);
        }
        WmsInnerInventoryDto innerInventoryDto = innerInventoryDtos.get(0);
        CheckShiftWorkBarcodeRecordDto recordDto = new CheckShiftWorkBarcodeRecordDto();
        recordDto.setWarehouseName(innerInventoryDto.getWarehouseName());
        recordDto.setMaterialCode(innerInventoryDto.getMaterialCode());
        recordDto.setMaterialName(innerInventoryDto.getMaterialName());
        recordDto.setPackingUnitName(innerInventoryDto.getPackingUnitName());
        recordDto.setPackageSpecificationQuantity(innerInventoryDto.getPackageSpecificationQuantity());
        recordDto.setMaterialQty(inventoryDetDto.getMaterialQty());
        recordDto.setWarehouseId(innerInventoryDto.getWarehouseId());
        recordDto.setStorageCode(innerInventoryDto.getStorageCode());
        recordDto.setStorageId(innerInventoryDto.getStorageId());
        recordDto.setMaterialId(innerInventoryDto.getMaterialId());
        if(dto.getJobOrderDetId() != null){
            WmsInnerJobOrderDet jobOrderDet = wmsInnerJobOrderDetService.selectByKey(dto.getJobOrderDetId());
            recordDto.setPlanQty(jobOrderDet.getPlanQty());
        }

        return recordDto;
    }

    @Override
    public int saveJobOrder(SaveShiftJobOrderDto dto) {
        BaseStorage baseStorage = baseFeignApi.detail(dto.getStorageId()).getData();
        if (baseStorage == null) {
            throw new BizErrorException(ErrorCodeEnum.PDA5001007);
        }
        WmsInnerJobOrder innerJobOrder = wmsInnerJobOrderService.selectByKey(dto.getJobOrderDetId());
        WmsInnerJobOrderDet wmsInnerJobOrderDet = wmsInnerJobOrderService.scanStorageBackQty(baseStorage.getStorageCode(), dto.getJobOrderDetId(), innerJobOrder.getActualQty());
        return 1;
    }


    /**
     * 获取当前登录用户
     *
     * @return
     */
    private SysUser currentUser() {
        SysUser user = CurrentUserInfoUtils.getCurrentUserInfo();
        if (StringUtils.isEmpty(user)) {
            throw new BizErrorException(ErrorCodeEnum.UAC10011039);
        }
        return user;
    }
}
