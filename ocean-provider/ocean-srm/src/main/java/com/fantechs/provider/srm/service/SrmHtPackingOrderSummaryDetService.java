package com.fantechs.provider.srm.service;

import com.fantechs.common.base.general.dto.srm.SrmHtPackingOrderSummaryDetDto;
import com.fantechs.common.base.general.entity.srm.history.SrmHtPackingOrderSummaryDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/27.
 */

public interface SrmHtPackingOrderSummaryDetService extends IService<SrmHtPackingOrderSummaryDet> {
    List<SrmHtPackingOrderSummaryDetDto> findList(Map<String, Object> map);
}
