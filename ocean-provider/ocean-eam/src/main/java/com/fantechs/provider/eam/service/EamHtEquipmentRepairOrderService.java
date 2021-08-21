package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentRepairOrder;
import com.fantechs.common.base.support.IService;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/20.
 */

public interface EamHtEquipmentRepairOrderService extends IService<EamHtEquipmentRepairOrder> {
    List<EamHtEquipmentRepairOrder> findList(Map<String, Object> map);
}
