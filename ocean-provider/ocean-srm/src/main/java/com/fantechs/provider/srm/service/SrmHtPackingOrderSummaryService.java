package com.fantechs.provider.srm.service;

import com.fantechs.common.base.general.dto.srm.SrmHtPackingOrderSummaryDto;
import com.fantechs.common.base.general.entity.srm.history.SrmHtPackingOrderSummary;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/27.
 */

public interface SrmHtPackingOrderSummaryService extends IService<SrmHtPackingOrderSummary> {
    List<SrmHtPackingOrderSummaryDto> findList(Map<String, Object> map);
}
