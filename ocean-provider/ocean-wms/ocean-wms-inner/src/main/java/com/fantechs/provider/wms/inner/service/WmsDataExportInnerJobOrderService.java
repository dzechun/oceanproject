package com.fantechs.provider.wms.inner.service;

import com.fantechs.common.base.general.dto.restapi.WmsDataExportInnerJobOrderDto;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerJobOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/09/01.
 */

public interface WmsDataExportInnerJobOrderService extends IService<WmsDataExportInnerJobOrderDto> {
    List<WmsDataExportInnerJobOrderDto> findExportData(Map<String, Object> map);

    String writeDeliveryDetails(WmsInnerJobOrder wmsInnerJobOrder, WmsInnerJobOrderDet wmsInnerJobOrderDet);
}
