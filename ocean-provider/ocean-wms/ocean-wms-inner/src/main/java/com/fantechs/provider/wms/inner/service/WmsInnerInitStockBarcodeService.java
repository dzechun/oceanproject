package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.entity.wms.inner.WmsInnerInitStockBarcode;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/12/01.
 */

public interface WmsInnerInitStockBarcodeService extends IService<WmsInnerInitStockBarcode> {
    List<WmsInnerInitStockBarcode> findList(Map<String, Object> map);
}
