package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.eng.EngPackingOrderTakeCancel;
import com.fantechs.common.base.general.dto.wms.inner.SaveHaveInnerJobOrderDto;
import com.fantechs.common.base.general.dto.wms.inner.SaveInnerJobOrderDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerJobOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrder;
import com.fantechs.common.base.support.IService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
/**
 *
 * Created by Mr.Lei on 2021/05/06.
 */

public interface WmsInnerJobOrderService extends IService<WmsInnerJobOrder> {
    List<WmsInnerJobOrderDto> findList(SearchWmsInnerJobOrder searchWmsInnerJobOrder);

    /**
     * 自动分配
     * @param ids
     * @return
     */
    int autoDistribution(String ids);

    /**
     * 手动分配
     * @param list
     * @return
     */
    int handDistribution(List<WmsInnerJobOrderDet> list);

    /**
     * 取消分配
     * @return
     */
    int cancelDistribution(String ids);

    /**
     * 关闭单据
     * @return
     */
    int closeWmsInnerJobOrder(String ids);

    /**
     * 指定工作人员
     * @return
     */
    int distributionWorker(Long jobOrderId,Long workerId);

    /**
     * 整单确认
     * @param ids
     * @return
     */
    int allReceiving(String ids);

    /**
     * 单一确认
     * @return
     */
    int singleReceiving(List<WmsInnerJobOrderDet> wmsInPutawayOrderDets);

    Map<String,Object> checkBarcode(String barCode,Long jobOrderDetId);

    WmsInnerJobOrderDet scanStorageBackQty(String storageCode, Long jobOrderDetId, BigDecimal qty, String barcode);

    WmsInnerJobOrder packageAutoAdd(WmsInnerJobOrder wmsInnerJobOrder);

    /**
     * PDA先单后作业 扫描检验条码
     * @param ifSysBarcode 是否系统条码
     * @param orderId 主表ID
     * @param orderDetId 明细ID
     * @param barCode 条码
     * @return
     */
    Map<String,Object> checkBarcodeHaveOrder(String ifSysBarcode,Long orderId,
                                             Long orderDetId, String barCode);

    /**
     * PDA先作业后单 扫描检验条码
     * @param ifSysBarcode 是否系统条码
     * @param barCode 条码
     * @return
     */
    Map<String,Object> checkBarcodeNotOrder(String ifSysBarcode,String barCode);

    /**
     * PDA先单后作业
     * @param list
     * @return
     */
    WmsInnerJobOrderDet saveHaveInnerJobOrder(List<SaveHaveInnerJobOrderDto> list);

    /**
     * PDA先作业后单 产生上架单
     * @param list
     * @return
     */
    WmsInnerJobOrder saveInnerJobOrder(List<SaveInnerJobOrderDto> list);

    /**
     * PDA激活关闭栈板
     * @param jobOrderId
     * @return
     */
    int activation(Long jobOrderId);

    /**
     * 移位单查询
     * @param map
     * @return
     */
    List<WmsInnerJobOrderDto> findShiftList(Map<String, Object> map);

    /**
     * 移位单批量删除
     * @param ids
     * @return
     */
    int batchDeleteByShiftWork(String ids);

    /**
     * 批量新增
     * @param list
     * @return
     */
    int addList(List<WmsInnerJobOrder> list);

    int cancelJobOrder(List<EngPackingOrderTakeCancel> engPackingOrderTakeCancels);

    /**
     * 库容入库规则判断入库数量
     * @param materialId
     * @param storageId
     * @param qty
     * @return
     */
    Boolean storageCapacity(Long materialId,Long storageId,BigDecimal qty);
}
