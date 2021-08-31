package com.fantechs.provider.srm.service;

import com.fantechs.common.base.general.dto.srm.SrmPackingOrderDto;
import com.fantechs.common.base.general.entity.srm.SrmPackingOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/27.
 */

public interface SrmPackingOrderService extends IService<SrmPackingOrder> {
    List<SrmPackingOrderDto> findList(Map<String, Object> map);
}
