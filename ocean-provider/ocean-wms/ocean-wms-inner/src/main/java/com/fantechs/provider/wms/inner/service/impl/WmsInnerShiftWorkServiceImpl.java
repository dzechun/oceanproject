package com.fantechs.provider.wms.inner.service.impl;

import com.fantechs.common.base.constants.ErrorCodeEnum;
import com.fantechs.common.base.entity.security.SysUser;
import com.fantechs.common.base.exception.BizErrorException;
import com.fantechs.common.base.general.dto.wms.inner.*;
import com.fantechs.common.base.general.entity.basic.BaseStorage;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcWorkOrderBarcode;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDetBarcode;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrderDet;
import com.fantechs.common.base.utils.CodeUtils;
import com.fantechs.common.base.utils.CurrentUserInfoUtils;
import com.fantechs.common.base.utils.StringUtils;
import com.fantechs.provider.api.base.BaseFeignApi;
import com.fantechs.provider.api.mes.sfc.SFCFeignApi;
import com.fantechs.provider.wms.inner.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.*;

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

    @Override
    public List<WmsInnerJobOrderDto> pdaFindList(String jobOrderCode) {
        Map<String, Object> map = new HashMap<>();
        map.put("jobOrderType", (byte) 2);
        if (StringUtils.isNotEmpty(jobOrderCode)) {
            map.put("jobOrderCode", jobOrderCode);
        }
        wmsInnerJobOrderService.findShiftList(map);
        return null;
    }

    @Override
    public List<WmsInnerJobOrderDetDto> pdaFindDetList(Long jobOrderId) {
        SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet = new SearchWmsInnerJobOrderDet();
        searchWmsInnerJobOrderDet.setJobOrderId(jobOrderId);
        searchWmsInnerJobOrderDet.setNonShiftStorageStatus((byte) 4);
        return wmsInnerJobOrderDetService.findList(searchWmsInnerJobOrderDet);
    }

    @Override
    @Transactional
    public int saveShiftWorkDetBarcode(SaveShiftWorkDetDto dto) {
        SysUser sysUser = currentUser();

        if (dto.getShiftWorkDetBarcodeDtos() == null && dto.getShiftWorkDetBarcodeDtos().size() <= 0) {
            throw new BizErrorException(ErrorCodeEnum.PDA5001006);
        }
        // 判断是否有作业单
        if (dto.getJobOrderId() != null) {

            List<WmsInnerJobOrderDet> jobOrderDetList = new ArrayList<>();
            List<WmsInnerJobOrderDetBarcode> jobOrderDetBarcodeList = new ArrayList<>();
            List<WmsInnerJobOrderDetBarcode> oldJobOrderDetBarcodeList = new ArrayList<>();
            for (SaveShiftWorkDetBarcodeDto shiftWorkDetBarcodeDto : dto.getShiftWorkDetBarcodeDtos()) {
                if (shiftWorkDetBarcodeDto.getJobOrderDetId() != null) {
                    // 移位作业明细单 变更移位状态
                    WmsInnerJobOrderDet jobOrderDet = wmsInnerJobOrderDetService.selectByKey(shiftWorkDetBarcodeDto.getJobOrderDetId());
                    jobOrderDet.setShiftStorageStatus((byte) 2);
                    jobOrderDetList.add(jobOrderDet);

                    // 删除原本移位单跟条码的关系
                    WmsInnerJobOrderDetBarcode oldOrderDetBarcode = wmsInnerJobOrderDetBarcodeService.findBarCode(shiftWorkDetBarcodeDto.getBarcode());
                    oldJobOrderDetBarcodeList.add(oldOrderDetBarcode);

                    // 查询条码
                    MesSfcWorkOrderBarcode workOrderBarcode = sfcFeignApi.findBarcode(shiftWorkDetBarcodeDto.getBarcode()).getData();
                    if (workOrderBarcode == null) {
                        throw new BizErrorException(ErrorCodeEnum.PDA40012000);
                    }
                    // 创建条码移位单明细关系
                    WmsInnerJobOrderDetBarcode wmsInnerJobOrderDetBarcode = new WmsInnerJobOrderDetBarcode();
                    wmsInnerJobOrderDetBarcode.setBarcode(shiftWorkDetBarcodeDto.getBarcode());
                    wmsInnerJobOrderDetBarcode.setJobOrderDetId(jobOrderDet.getJobOrderDetId());
                    wmsInnerJobOrderDetBarcode.setWorkOrderBarcodeId(workOrderBarcode.getWorkOrderBarcodeId());
                    wmsInnerJobOrderDetBarcode.setStatus((byte) 1);
                    wmsInnerJobOrderDetBarcode.setOrgId(sysUser.getOrganizationId());
                    wmsInnerJobOrderDetBarcode.setCreateTime(new Date());
                    wmsInnerJobOrderDetBarcode.setCreateUserId(sysUser.getUserId());
                    wmsInnerJobOrderDetBarcode.setIsDelete((byte) 1);
                    jobOrderDetBarcodeList.add(wmsInnerJobOrderDetBarcode);
                }
            }

            if (jobOrderDetList.size() > 0) {
                wmsInnerJobOrderDetService.batchUpdate(jobOrderDetList);
            }
            if (oldJobOrderDetBarcodeList.size() > 0) {
                wmsInnerJobOrderDetBarcodeService.batchDelete(oldJobOrderDetBarcodeList);
            }
            if (jobOrderDetBarcodeList.size() > 0) {
                wmsInnerJobOrderDetBarcodeService.batchSave(jobOrderDetBarcodeList);
            }

            // 作业单拣货数量+1以及变更单据状态
            WmsInnerJobOrder innerJobOrder = wmsInnerJobOrderService.selectByKey(dto.getJobOrderId());
            innerJobOrder.setOrderStatus((byte) 4);
            innerJobOrder.setActualQty(innerJobOrder.getActualQty() != null ? innerJobOrder.getActualQty().add(new BigDecimal(dto.getShiftWorkDetBarcodeDtos().size())) : BigDecimal.ONE);
            wmsInnerJobOrderService.update(innerJobOrder);
        } else {
            // 创建移位单
            WmsInnerJobOrder innerJobOrder = new WmsInnerJobOrder();
            innerJobOrder.setWarehouseId(dto.getWarehouseId());
            innerJobOrder.setWorkerId(sysUser.getUserId());
            innerJobOrder.setJobOrderCode(CodeUtils.getId("SHIFT-"));
            innerJobOrder.setJobOrderType((byte) 2);
            if(innerJobOrder.getPlanQty() != null){
                innerJobOrder.setPlanQty(innerJobOrder.getPlanQty().add(new BigDecimal(dto.getShiftWorkDetBarcodeDtos().size())));
            } else {
                innerJobOrder.setPlanQty(new BigDecimal(dto.getShiftWorkDetBarcodeDtos().size()));
            }
            if(innerJobOrder.getActualQty() != null){
                innerJobOrder.setActualQty(innerJobOrder.getActualQty().add(new BigDecimal(dto.getShiftWorkDetBarcodeDtos().size())));
            } else {
                innerJobOrder.setActualQty(new BigDecimal(dto.getShiftWorkDetBarcodeDtos().size()));
            }
            innerJobOrder.setOrderStatus((byte) 3);
            innerJobOrder.setStatus((byte) 1);
            innerJobOrder.setOrgId(sysUser.getOrganizationId());
            innerJobOrder.setCreateTime(new Date());
            innerJobOrder.setCreateUserId(sysUser.getUserId());
            innerJobOrder.setIsDelete((byte) 1);
            wmsInnerJobOrderService.save(innerJobOrder);

            for (SaveShiftWorkDetBarcodeDto shiftWorkDetBarcodeDto : dto.getShiftWorkDetBarcodeDtos()){
                // 查询库存信息，同一库位跟同物料有且只有一条数据
                Map<String, Object> map = new HashMap<>();
                map.put("materialId", shiftWorkDetBarcodeDto.getMaterialId());
                map.put("storageId", shiftWorkDetBarcodeDto.getStorageId());
                map.put("storageId", shiftWorkDetBarcodeDto.getStorageId());
                map.put("storageId", shiftWorkDetBarcodeDto.getStorageId());
                map.put("storageId", shiftWorkDetBarcodeDto.getStorageId());
                map.put("storageId", shiftWorkDetBarcodeDto.getStorageId());
                wmsInnerInventoryService.findList(map);
            }
            // 创建移位明细
            // 创建库存
            // 创建库存明细
            // 创建条码移位单明细关系
        }

        return 1;
    }

    @Override
    public WmsInnerInventoryDetDto CheckShiftWorkBarcode(CheckShiftWorkBarcodeDto dto) {
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
