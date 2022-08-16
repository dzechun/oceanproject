package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDetBarcode;
import com.fantechs.common.base.support.IService;

/**
 *
 * Created by Mr.Lei on 2021/05/07.
 */

public interface WmsInnerJobOrderDetBarcodeService extends IService<WmsInnerJobOrderDetBarcode> {
    WmsInnerJobOrderDetBarcode findBarCode(String barCode);
}
