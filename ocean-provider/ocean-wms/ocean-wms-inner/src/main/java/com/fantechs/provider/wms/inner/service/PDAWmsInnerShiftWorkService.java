package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.*;
import com.fantechs.common.base.general.entity.wms.inner.search.SearchWmsInnerJobOrderDet;

import java.util.List;
import java.util.Map;

/**
 * 仓库作业-库内移位作业
 */
public interface PDAWmsInnerShiftWorkService {

    /**
     * 查找移位作业单
     * @param map
     * @return
     */
    List<WmsInnerJobOrderDto> pdaFindList(Map<String, Object> map);

    /**
     * 查找移位作业明细单
     * @param searchWmsInnerJobOrderDet
     * @return
     */
    List<WmsInnerJobOrderDetDto> pdaFindDetList(SearchWmsInnerJobOrderDet searchWmsInnerJobOrderDet);

    /**
     * 移位作业捡货确认
     * @param dto
     * @return
     */
    String saveShiftWorkDetBarcode(SaveShiftWorkDetDto dto);

    /**
     * 校验条码库位
     * @param dto
     * @return
     */
    CheckShiftWorkBarcodeRecordDto checkShiftWorkBarcode(CheckShiftWorkBarcodeDto dto);

    /**
     * 移位作业上架确认
     * @param dto
     * @return
     */
    int saveJobOrder(SaveShiftJobOrderDto dto);
}
