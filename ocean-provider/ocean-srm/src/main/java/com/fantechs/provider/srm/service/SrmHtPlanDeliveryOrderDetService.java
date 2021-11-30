package com.fantechs.provider.srm.service;

import com.fantechs.common.base.general.entity.srm.history.SrmHtPlanDeliveryOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/29.
 */

public interface SrmHtPlanDeliveryOrderDetService extends IService<SrmHtPlanDeliveryOrderDet> {

    List<SrmHtPlanDeliveryOrderDet> findList(Map<String, Object> map);

}
