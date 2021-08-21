package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamEquipmentMaintainProjectItemDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaintainProjectItem;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/08/21.
 */

public interface EamEquipmentMaintainProjectItemService extends IService<EamEquipmentMaintainProjectItem> {
    List<EamEquipmentMaintainProjectItemDto> findList(Map<String, Object> map);
    List<EamEquipmentMaintainProjectItemDto> findHtList(Map<String, Object> map);

    /**
     * 批量新增保养项目事项以及其履历表
     * @param items
     * @return
     */
    int batchSave(List<EamEquipmentMaintainProjectItem> items);
}
