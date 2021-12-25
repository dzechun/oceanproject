package com.fantechs.provider.wms.in.service;

import com.fantechs.common.base.general.dto.wms.in.WmsInHtInPlanOrderDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInHtInPlanOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/12/08.
 */

public interface WmsInHtInPlanOrderService extends IService<WmsInHtInPlanOrder> {
    List<WmsInHtInPlanOrderDto> findList(Map<String, Object> map);
}
