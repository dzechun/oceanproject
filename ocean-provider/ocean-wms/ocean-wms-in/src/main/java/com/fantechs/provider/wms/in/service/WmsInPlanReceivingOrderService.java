package com.fantechs.provider.wms.in.service;

import com.fantechs.common.base.general.dto.wms.in.WmsInPlanReceivingOrderDto;
import com.fantechs.common.base.general.dto.wms.in.imports.WmsInPlanReceivingOrderImport;
import com.fantechs.common.base.general.entity.wms.in.WmsInPlanReceivingOrder;
import com.fantechs.common.base.general.entity.wms.in.WmsInPlanReceivingOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by mr.lei on 2021/12/13.
 */

public interface WmsInPlanReceivingOrderService extends IService<WmsInPlanReceivingOrder> {
    List<WmsInPlanReceivingOrderDto> findList(Map<String, Object> map);

    Map<String, Object> importExcel(List<WmsInPlanReceivingOrderImport> list);

    int pushDown(List<WmsInPlanReceivingOrderDet> wmsInPlanReceivingOrderDets);
}
