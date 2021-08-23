package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamEquipmentMaintainOrderDetDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaintainOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/08/21.
 */

public interface EamEquipmentMaintainOrderDetService extends IService<EamEquipmentMaintainOrderDet> {
    List<EamEquipmentMaintainOrderDetDto> findList(Map<String, Object> map);

    List<EamEquipmentMaintainOrderDetDto> findHtList(Map<String, Object> map);

    /**
     * 批量新增点检单明细以及其履历表
     * @param items
     * @return
     */
    int batchSave(List<EamEquipmentMaintainOrderDet> items);
}
