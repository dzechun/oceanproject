package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.entity.eam.history.EamHtJigRepairOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/16.
 */

public interface EamHtJigRepairOrderService extends IService<EamHtJigRepairOrder> {
    List<EamHtJigRepairOrder> findHtList(Map<String, Object> map);
}
