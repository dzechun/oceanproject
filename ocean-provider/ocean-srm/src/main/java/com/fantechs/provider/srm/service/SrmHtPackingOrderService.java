package com.fantechs.provider.srm.service;

import com.fantechs.common.base.general.dto.srm.SrmHtPackingOrderDto;
import com.fantechs.common.base.general.entity.srm.history.SrmHtPackingOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/27.
 */

public interface SrmHtPackingOrderService extends IService<SrmHtPackingOrder> {
    List<SrmHtPackingOrderDto> findList(Map<String, Object> map);
}
