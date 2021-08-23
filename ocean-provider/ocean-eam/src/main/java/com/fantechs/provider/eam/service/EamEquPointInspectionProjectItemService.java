package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamEquPointInspectionProjectItemDto;
import com.fantechs.common.base.general.entity.eam.EamEquPointInspectionProjectItem;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/08/20.
 */

public interface EamEquPointInspectionProjectItemService extends IService<EamEquPointInspectionProjectItem> {
    List<EamEquPointInspectionProjectItemDto> findList(Map<String, Object> map);

    List<EamEquPointInspectionProjectItemDto> findHtList(Map<String, Object> map);

    /**
     * 批量新增点检项目事项以及其履历表
     * @param items
     * @return
     */
    int batchSave(List<EamEquPointInspectionProjectItem> items);
}
