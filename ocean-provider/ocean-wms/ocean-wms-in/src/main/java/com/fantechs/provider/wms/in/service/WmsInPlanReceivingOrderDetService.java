package com.fantechs.provider.wms.in.service;

import com.fantechs.common.base.general.dto.wms.in.WmsInPlanReceivingOrderDetDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInPlanReceivingOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by mr.lei on 2021/12/13.
 */

public interface WmsInPlanReceivingOrderDetService extends IService<WmsInPlanReceivingOrderDet> {
    List<WmsInPlanReceivingOrderDetDto> findList(Map<String, Object> map);
}
