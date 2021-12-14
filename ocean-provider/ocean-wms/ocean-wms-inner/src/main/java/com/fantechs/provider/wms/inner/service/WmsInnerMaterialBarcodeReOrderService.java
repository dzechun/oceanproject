package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.wms.inner.WmsInnerMaterialBarcodeReOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerMaterialBarcodeReOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

public interface WmsInnerMaterialBarcodeReOrderService extends IService<WmsInnerMaterialBarcodeReOrder> {
    List<WmsInnerMaterialBarcodeReOrderDto> findList(Map<String, Object> map);

    int batchAdd(List<WmsInnerMaterialBarcodeReOrder> list);
}
