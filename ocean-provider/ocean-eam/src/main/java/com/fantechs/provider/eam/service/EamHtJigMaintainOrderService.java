package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.entity.eam.history.EamHtJigMaintainOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/13.
 */

public interface EamHtJigMaintainOrderService extends IService<EamHtJigMaintainOrder> {
    List<EamHtJigMaintainOrder> findHtList(Map<String, Object> map);
}
