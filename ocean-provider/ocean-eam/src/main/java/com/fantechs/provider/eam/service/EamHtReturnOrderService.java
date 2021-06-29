package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.entity.eam.history.EamHtReturnOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/29.
 */

public interface EamHtReturnOrderService extends IService<EamHtReturnOrder> {
    List<EamHtReturnOrder> findHtList(Map<String, Object> map);
}
