package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamEquipmentRepairOrderDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentRepairOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/08/20.
 */

public interface EamEquipmentRepairOrderService extends IService<EamEquipmentRepairOrder> {
    List<EamEquipmentRepairOrderDto> findList(Map<String, Object> map);

    int save(EamEquipmentRepairOrderDto record);

    int update(EamEquipmentRepairOrderDto record);
}
