package com.fantechs.provider.srm.service;

import com.fantechs.common.base.general.dto.srm.SrmPlanDeliveryOrderDetDto;
import com.fantechs.common.base.general.entity.srm.SrmPlanDeliveryOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/11/29.
 */

public interface SrmPlanDeliveryOrderDetService extends IService<SrmPlanDeliveryOrderDet> {
    List<SrmPlanDeliveryOrderDetDto> findList(Map<String, Object> map);

    int asn(List<SrmPlanDeliveryOrderDetDto> list);
}
