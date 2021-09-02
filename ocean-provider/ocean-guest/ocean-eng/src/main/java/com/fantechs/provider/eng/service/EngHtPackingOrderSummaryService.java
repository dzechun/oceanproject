package com.fantechs.provider.eng.service;

import com.fantechs.common.base.general.dto.eng.EngHtPackingOrderSummaryDto;
import com.fantechs.common.base.general.entity.eng.history.EngHtPackingOrderSummary;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/27.
 */

public interface EngHtPackingOrderSummaryService extends IService<EngHtPackingOrderSummary> {
    List<EngHtPackingOrderSummaryDto> findList(Map<String, Object> map);
}
