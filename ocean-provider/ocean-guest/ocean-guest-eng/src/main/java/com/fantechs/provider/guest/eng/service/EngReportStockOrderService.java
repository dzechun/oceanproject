package com.fantechs.provider.guest.eng.service;

import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrder;
import com.fantechs.common.base.general.entity.wms.inner.WmsInnerStockOrderDet;

import java.util.List;

/**
 *
 * Created by leifengzhi on 2021/09/01.
 */

public interface EngReportStockOrderService {

    String reportStockOrder(List<WmsInnerStockOrderDet> WmsInnerStockOrderDets , WmsInnerStockOrder wmsInnerStockOrder );
}
