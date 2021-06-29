package com.fantechs.provider.wms.inner.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.*;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcode;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDetBarcode;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrderDet;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.pm.PMFeignApi;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.wms.inner.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.*;

@Service
public class WmsInnerShiftWorkServiceImpl implements WmsInnerShiftWorkService {

    @Autowired
    WmsInnerJobOrderService wmsInnerJobOrderService;

    @Autowired
    WmsInnerJobOrderDetService wmsInnerJobOrderDetService;

    @Autowired
    WmsInnerJobOrderDetBarcodeService wmsInnerJobOrderDetBarcodeService;

    @Autowired
    WmsInnerInventoryDetService wmsInnerInventoryDetService;

    @Autowired
    WmsInnerInventoryService wmsInnerInventoryService;

    @Autowired
    BaseFeignApi baseFeignApi;

    @Autowired
    SFCFeignApi sfcFeignApi;

    @Autowired
    PMFeignApi pmFeignApi;

    @Override
    public List<WmsInnerJobOrderDto> pdaFindList(Map<String, Object> map) {
        map.put("jobOrderType", (byte) 2);
        if (StringUtils.isNotEmpty(map.get("jobOrderCode"))) {
            map.put("jobOrderCode", map.get("jobOrderCode"));
        }
        wmsInnerJobOrderService.findShiftList(map);
        return null;
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
            List<WmsInnerJobOrderDetBarcode> jobOrderDetBarcodeList = new ArrayList<>();
            List<WmsInnerJobOrderDetBarcode> oldJobOrderDetBarcodeList = new ArrayList<>();
            for (String barcode : dto.getBarcodes()) {
                // 删除原本移位单跟条码的关系
                WmsInnerJobOrderDetBarcode oldOrderDetBarcode = wmsInnerJobOrderDetBarcodeService.findBarCode(barcode);
                oldJobOrderDetBarcodeList.add(oldOrderDetBarcode);

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
            }

            // 移位作业明细单 变更移位状态
            WmsInnerJobOrderDet jobOrderDet = wmsInnerJobOrderDetService.selectByKey(dto.getJobOrderDetId());
            jobOrderDet.setShiftStorageStatus((byte) 2);
            wmsInnerJobOrderDetService.update(jobOrderDet);

            if (oldJobOrderDetBarcodeList.size() > 0) {
                wmsInnerJobOrderDetBarcodeService.batchDelete(oldJobOrderDetBarcodeList);
            }
            if (jobOrderDetBarcodeList.size() > 0) {
                wmsInnerJobOrderDetBarcodeService.batchSave(jobOrderDetBarcodeList);
            }

            // 作业单拣货数量+1以及变更单据状态
            WmsInnerJobOrder innerJobOrder = wmsInnerJobOrderService.selectByKey(dto.getJobOrderId());
            innerJobOrder.setOrderStatus((byte) 4);
            innerJobOrder.setActualQty(innerJobOrder.getActualQty() != null ? innerJobOrder.getActualQty().add(new BigDecimal(dto.getBarcodes().size())) : BigDecimal.ONE);
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
            if (innerInventoryDto.getPackingQty().compareTo(new BigDecimal(dto.getBarcodes().size())) < -1){
                throw new BizErrorException(ErrorCodeEnum.PDA5001012);
            }

            if(dto.getJobOrderId() != null){
                // 更新移位单
                WmsInnerJobOrder innerJobOrder = wmsInnerJobOrderService.selectByKey(dto.getJobOrderId());
                innerJobOrder.setOrderStatus((byte) 4);
                innerJobOrder.setActualQty(innerJobOrder.getActualQty() != null ? innerJobOrder.getActualQty().add(new BigDecimal(dto.getBarcodes().size())) : BigDecimal.ONE);
                innerJobOrder.setPlanQty(innerJobOrder.getActualQty());
                wmsInnerJobOrderService.update(innerJobOrder);
            }else {
                // 创建移位单
                WmsInnerJobOrder innerJobOrder = new WmsInnerJobOrder();
                innerJobOrder.setWarehouseId(dto.getWarehouseId());
                innerJobOrder.setWorkerId(sysUser.getUserId());
                innerJobOrder.setJobOrderCode(CodeUtils.getId("SHIFT-"));
                innerJobOrder.setJobOrderType((byte) 2);
                innerJobOrder.setPlanQty(new BigDecimal(dto.getBarcodes().size()));
                innerJobOrder.setActualQty(innerJobOrder.getPlanQty());
                innerJobOrder.setOrderStatus((byte) 4);
                innerJobOrder.setStatus((byte) 1);
                innerJobOrder.setOrgId(sysUser.getOrganizationId());
                innerJobOrder.setCreateTime(new Date());
                innerJobOrder.setCreateUserId(sysUser.getUserId());
                innerJobOrder.setIsDelete((byte) 1);
                wmsInnerJobOrderService.save(innerJobOrder);
                dto.setJobOrderId(innerJobOrder.getJobOrderId());
            }

            // 创建移位明细
            WmsInnerJobOrderDet wmsInnerJobOrderDet = new WmsInnerJobOrderDet();
            wmsInnerJobOrderDet.setJobOrderId(dto.getJobOrderId());
            wmsInnerJobOrderDet.setMaterialOwnerId(innerInventoryDto.getMaterialOwnerId());
            wmsInnerJobOrderDet.setWarehouseId(dto.getWarehouseId());
            wmsInnerJobOrderDet.setOutStorageId(dto.getStorageId());
//            wmsInnerJobOrderDet
            // 新增待出库存信息
            WmsInnerInventoryDto newInnerInventoryDto = new WmsInnerInventoryDto();
            BeanUtil.copyProperties(innerInventoryDto, newInnerInventoryDto);
            newInnerInventoryDto.setJobStatus((byte) 3);
            newInnerInventoryDto.setJobOrderDetId(wmsInnerJobOrderDet.getJobOrderDetId());
            newInnerInventoryDto.setOrgId(sysUser.getOrganizationId());
            newInnerInventoryDto.setCreateTime(new Date());
            newInnerInventoryDto.setCreateUserId(sysUser.getUserId());
            wmsInnerInventoryService.save(newInnerInventoryDto);
            // 变更减少原库存

            for (String barcode : dto.getBarcodes()){


            }
            // 创建库存明细
            // 创建条码移位单明细关系
        }

        return "1";
    }

    @Override
    public WmsInnerInventoryDetDto checkShiftWorkBarcode(CheckShiftWorkBarcodeDto dto) {
        if (StringUtils.isEmpty(dto.getBarcode())) {
            throw new BizErrorException(ErrorCodeEnum.PDA5001001);
        }
        MesSfcWorkOrderBarcode workOrderBarcode = sfcFeignApi.findBarcode(dto.getBarcode()).getData();
        if (workOrderBarcode == null) {
            throw new BizErrorException(ErrorCodeEnum.PDA40012000);
        }
        if (StringUtils.isEmpty(dto.getStorageCode())) {
            throw new BizErrorException(ErrorCodeEnum.PDA5001003);
        }
        if (dto.getJobOrderDetId() != null) {
            WmsInnerJobOrderDet jobOrderDet = wmsInnerJobOrderDetService.selectByKey(dto.getJobOrderDetId());
            BaseStorage baseStorage = baseFeignApi.detail(jobOrderDet.getOutStorageId()).getData();
            if (baseStorage == null) {
                throw new BizErrorException(ErrorCodeEnum.PDA5001007);
            }
            if (!baseStorage.getStorageCode().equals(dto.getStorageCode())) {
                throw new BizErrorException(ErrorCodeEnum.PDA5001000);
            }
            if(!baseStorage.getStorageType().equals("1")){
                throw new BizErrorException(ErrorCodeEnum.PDA5001008);
            }
        }
        if (dto.getMaterialId() != null){
            // 查询条码
            MesPmWorkOrder pmWorkOrder = pmFeignApi.workOrderDetail(workOrderBarcode.getWorkOrderId()).getData();
            if(pmWorkOrder == null){
                throw new BizErrorException(ErrorCodeEnum.PDA5001010);
            }
            if(!dto.getMaterialId().equals(pmWorkOrder.getMaterialId())){
                throw new BizErrorException(ErrorCodeEnum.PDA5001011);
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("storageCode", dto.getStorageCode());
        map.put("barcode", dto.getBarcode());
        List<WmsInnerInventoryDetDto> inventoryDetDtos = wmsInnerInventoryDetService.findList(map);
        if (inventoryDetDtos == null || inventoryDetDtos.size() <= 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA5001004);
        }
        WmsInnerInventoryDetDto inventoryDetDto = inventoryDetDtos.get(0);
        if (dto.getWarehouseId() != null && !inventoryDetDto.getWarehouseId().equals(dto.getWarehouseId())) {
            throw new BizErrorException(ErrorCodeEnum.PDA5001005);
        }

        return inventoryDetDto;
    }

    @Override
    public int saveJobOrder(SaveShiftJobOrderDto dto) {
        // 判断是否已有上架单
        if (dto.getJobOrderId() != null){
            // 创建上架单
        }else {
            // 变更上架单数量
        }
        // 创建上架明细单
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
