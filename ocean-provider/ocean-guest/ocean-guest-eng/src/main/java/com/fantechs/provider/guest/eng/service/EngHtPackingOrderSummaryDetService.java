package com.fantechs.provider.guest.eng.service;

import com.fantechs.common.base.general.dto.eng.EngHtPackingOrderSummaryDetDto;
import com.fantechs.common.base.general.entity.eng.history.EngHtPackingOrderSummaryDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/27.
 */

public interface EngHtPackingOrderSummaryDetService extends IService<EngHtPackingOrderSummaryDet> {
    List<EngHtPackingOrderSummaryDetDto> findList(Map<String, Object> map);
}
