package com.fantechs.provider.wms.out.service;

import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanDeliveryOrderDetDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPlanDeliveryOrderDet;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtPlanDeliveryOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/12/22.
 */

public interface WmsOutPlanDeliveryOrderDetService extends IService<WmsOutPlanDeliveryOrderDet> {
    List<WmsOutPlanDeliveryOrderDetDto> findList(Map<String, Object> map);
    List<WmsOutHtPlanDeliveryOrderDet> findHtList(Map<String, Object> map);
    int updateActualQty(WmsOutPlanDeliveryOrderDetDto wmsOutPlanDeliveryOrderDetDto);
}
