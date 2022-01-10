package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerStockOrderDetBarcodeDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrderDetBarcode;
import com.fantechs.common.base.support.IService;


import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/12/28.
 */

public interface WmsInnerStockOrderDetBarcodeService extends IService<WmsInnerStockOrderDetBarcode> {
    List<WmsInnerStockOrderDetBarcodeDto> findList(Map<String, Object> map);

    int save(WmsInnerStockOrderDetBarcodeDto record);
}
