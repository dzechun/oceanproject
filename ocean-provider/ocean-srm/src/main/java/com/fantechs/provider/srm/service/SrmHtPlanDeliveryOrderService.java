package com.fantechs.provider.srm.service;

import com.fantechs.common.base.general.entity.srm.history.SrmHtPlanDeliveryOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/29.
 */

public interface SrmHtPlanDeliveryOrderService extends IService<SrmHtPlanDeliveryOrder> {
    List<SrmHtPlanDeliveryOrder> findList(Map<String, Object> map);
}
