package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.*;

import java.util.List;

/**
 * 仓库作业-库内移位作业
 */
public interface WmsInnerShiftWorkService {

    /**
     * 查找移位作业单
     * @param jobOrderCode
     * @return
     */
    List<WmsInnerJobOrderDto> pdaFindList(String jobOrderCode);

    /**
     * 查找移位作业明细单
     * @param jobOrderId
     * @return
     */
    List<WmsInnerJobOrderDetDto> pdaFindDetList(Long jobOrderId);

    /**
     * 校验移位明细单
     * @param dto
     * @return
     */
    int saveShiftWorkDetBarcode(SaveShiftWorkDetDto dto);

    /**
     * 校验条码库位
     * @param dto
     * @return
     */
    WmsInnerInventoryDetDto CheckShiftWorkBarcode(CheckShiftWorkBarcodeDto dto);
}
