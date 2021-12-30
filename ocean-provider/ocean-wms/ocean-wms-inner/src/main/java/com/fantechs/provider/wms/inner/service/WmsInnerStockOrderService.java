package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.AddInnerStockDetDto;
import com.fantechs.common.base.general.dto.wms.inner.BarcodeResultDto;
import com.fantechs.common.base.general.dto.wms.inner.CommitInnerStockBarcodeDto;
import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStockOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrderDet;
import com.fantechs.common.base.support.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
/**
 *
 * Created by Mr.Lei on 2021/05/27.
 */

public interface WmsInnerStockOrderService extends IService<WmsInnerStockOrder> {
    List<WmsInnerStockOrderDto> findList(Map<String, Object> map);

    /**
     * 盘点单激活或作废
     * @param ids
     * @return
     */
    int activation(String ids,Integer btnType);

    /**
     * 盘点确认
     * @param ids
     * @return
     */
    int ascertained(String ids);

    /**
     * 差异处理
     * @param ids
     * @return
     */
    int difference(String ids);

    /**
     * PDA盘点确认
     * @return
     */
    int pdaConfirm(String ids);

    /**
     * PDA扫条码返回数量
     * @param barcode
     * @return
     */
    BarcodeResultDto scanBarcode(Long stockOrderDetId, String barcode);

    /**
     * PDA扫条码提交
     * @param
     * @return
     */
    int pdaCommit(WmsInnerStockOrderDet wmsInnerStockOrderDet,List<CommitInnerStockBarcodeDto> barcodeList);

    /**
     * PDA盘点增补
     * @param
     * @return
     */
    int addInnerStockDet(List<AddInnerStockDetDto> addDetList);
}
