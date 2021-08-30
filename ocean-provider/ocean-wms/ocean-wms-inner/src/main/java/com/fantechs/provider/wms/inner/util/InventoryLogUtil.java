package com.fantechs.provider.wms.inner.util;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerInventoryLogDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrder;
import com.fantechs.common.base.general.entity.wms.in.WmsInAsnOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.*;
import com.fantechs.provider.wms.inner.service.WmsInnerInventoryLogService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author mr.lei
 * @Date 2021/7/29
 */
@Component
public class InventoryLogUtil {
    @Resource
    private WmsInnerInventoryLogService wmsInnerInventoryLogService;

    private static InventoryLogUtil inventoryLogUtil;

    @PostConstruct
    public void init(){
        inventoryLogUtil = this;
        inventoryLogUtil.wmsInnerInventoryLogService = wmsInnerInventoryLogService;
    }

    public static void addLog(WmsInAsnOrder wmsInAsnOrder, WmsInAsnOrderDet wmsInAsnOrderDet){
        WmsInnerInventoryLog wmsInnerInventoryLog = new WmsInnerInventoryLogDto();
        wmsInnerInventoryLog.setAsnCode(wmsInAsnOrder.getAsnCode());
        //收货
        wmsInnerInventoryLog.setJobOrderType((byte)1);
        wmsInnerInventoryLog.setAddOrSubtract((byte)1);
        wmsInnerInventoryLog.setProductionDate(wmsInAsnOrderDet.getProductionDate());
        wmsInnerInventoryLog.setExpiredDate(wmsInAsnOrderDet.getExpiredDate());
        wmsInnerInventoryLog.setBatchCode(wmsInAsnOrderDet.getBatchCode());
        wmsInnerInventoryLog.setPalletCode(wmsInAsnOrderDet.getPalletCode());
        wmsInnerInventoryLog.setInventoryStatusId(wmsInAsnOrderDet.getInventoryStatusId());
        wmsInnerInventoryLog.setChangeQty(wmsInAsnOrderDet.getActualQty());
        wmsInnerInventoryLog.setSupplierId(wmsInAsnOrder.getSupplierId());
        wmsInnerInventoryLog.setMaterialOwnerId(wmsInAsnOrder.getMaterialOwnerId());
        wmsInnerInventoryLog.setInventoryStatusName(inventoryLogUtil.wmsInnerInventoryLogService.findInvName(wmsInnerInventoryLog.getInventoryLogId()));
    }

    /**
     *
     * @param wmsInnerJobOrder 拣货/上架
     * @param wmsInnerJobOrderDet 拣货/上架
     * @param jobStatus 作业类型(1-收货，2-上架，3-移位，4-拣货，5-补货，6-调整，7-盘点，8-发运)
     * @param addOrSubtract 加减类型(1-加 2-减)
     */
    public static void addLog(WmsInnerJobOrder wmsInnerJobOrder, WmsInnerJobOrderDet wmsInnerJobOrderDet, Byte jobStatus, Byte addOrSubtract){
        WmsInnerInventoryLog wmsInnerInventoryLog = new WmsInnerInventoryLogDto();
        wmsInnerInventoryLog.setAsnCode(wmsInnerJobOrder.getRelatedOrderCode());
        wmsInnerInventoryLog.setRelatedOrderCode(wmsInnerJobOrder.getJobOrderCode());
        //收货
        wmsInnerInventoryLog.setJobOrderType(jobStatus);
        wmsInnerInventoryLog.setAddOrSubtract(addOrSubtract);
        wmsInnerInventoryLog.setStorageId(wmsInnerJobOrderDet.getInStorageId());
        wmsInnerInventoryLog.setWarehouseId(wmsInnerJobOrderDet.getWarehouseId());
        wmsInnerInventoryLog.setMaterialId(wmsInnerJobOrderDet.getMaterialId());
        wmsInnerInventoryLog.setProductionDate(wmsInnerJobOrderDet.getProductionDate());
        wmsInnerInventoryLog.setExpiredDate(wmsInnerJobOrderDet.getExpiredDate());
        wmsInnerInventoryLog.setBatchCode(wmsInnerJobOrderDet.getBatchCode());
        wmsInnerInventoryLog.setPalletCode(wmsInnerJobOrderDet.getPalletCode());
        wmsInnerInventoryLog.setInventoryStatusId(wmsInnerJobOrderDet.getInventoryStatusId());
        wmsInnerInventoryLog.setChangeQty(wmsInnerJobOrderDet.getActualQty());
        wmsInnerInventoryLog.setMaterialOwnerId(wmsInnerJobOrder.getMaterialOwnerId());
        inventoryLogUtil.wmsInnerInventoryLogService.save(wmsInnerInventoryLog);
    }

    /**
     * 盘点日志记录
     * @param wmsInnerStockOrder
     * @param wmsInnerInventory
     * @param addOrSubtract
     */
    public static void addLog(WmsInnerStockOrder wmsInnerStockOrder, WmsInnerInventory wmsInnerInventory, Byte addOrSubtract){
        WmsInnerInventoryLog wmsInnerInventoryLog = new WmsInnerInventoryLogDto();
        wmsInnerInventoryLog.setRelatedOrderCode(wmsInnerStockOrder.getStockOrderCode());
        //收货
        wmsInnerInventoryLog.setJobOrderType((byte)7);
        wmsInnerInventoryLog.setAddOrSubtract(addOrSubtract);
        wmsInnerInventoryLog.setProductionDate(wmsInnerInventory.getProductionDate());
        wmsInnerInventoryLog.setExpiredDate(wmsInnerInventory.getExpiredDate());
        wmsInnerInventoryLog.setBatchCode(wmsInnerInventory.getBatchCode());
        wmsInnerInventoryLog.setPalletCode(wmsInnerInventory.getPalletCode());
        wmsInnerInventoryLog.setInventoryStatusId(wmsInnerInventory.getInventoryStatusId());
        wmsInnerInventoryLog.setChangeQty(wmsInnerInventory.getPackingQty());
        wmsInnerInventoryLog.setMaterialOwnerId(wmsInnerInventory.getMaterialOwnerId());
        wmsInnerInventoryLog.setInventoryStatusName(inventoryLogUtil.wmsInnerInventoryLogService.findInvName(wmsInnerInventoryLog.getInventoryLogId()));
        //期初数量
        Map<String,Object> map = paramUtil(wmsInnerInventoryLog);
        wmsInnerInventoryLog.setInitialQty(inventoryLogUtil.wmsInnerInventoryLogService.findInv(map));
        wmsInnerInventoryLog.setFinalQty(wmsInnerInventoryLog.getInitialQty().add(wmsInnerInventoryLog.getChangeQty()));
        inventoryLogUtil.wmsInnerInventoryLogService.save(wmsInnerInventoryLog);
    }


    private static Map<String,Object> paramUtil(WmsInnerInventoryLog wmsInnerInventoryLog){
        Map<String,Object> map = new HashMap<>();
        map.put("warehouseId",wmsInnerInventoryLog.getWarehouseId());
        map.put("storageId",wmsInnerInventoryLog.getStorageId());
        map.put("materialId",wmsInnerInventoryLog.getMaterialId());
        map.put("batchCode",wmsInnerInventoryLog.getBatchCode());
        map.put("inventoryStatusId",wmsInnerInventoryLog.getInventoryStatusId());
        return map;
    }
}