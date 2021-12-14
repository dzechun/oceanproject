package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.entity.wms.inner.history.WmsInnerHtMaterialBarcodeReOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

public interface WmsInnerHtMaterialBarcodeReOrderService extends IService<WmsInnerHtMaterialBarcodeReOrder> {
    List<WmsInnerHtMaterialBarcodeReOrder> findList(Map<String, Object> map);
}
