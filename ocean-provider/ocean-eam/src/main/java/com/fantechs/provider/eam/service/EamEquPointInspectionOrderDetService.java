package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamEquPointInspectionOrderDetDto;
import com.fantechs.common.base.general.entity.eam.EamEquPointInspectionOrderDet;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/08/20.
 */

public interface EamEquPointInspectionOrderDetService extends IService<EamEquPointInspectionOrderDet> {
    List<EamEquPointInspectionOrderDetDto> findList(Map<String, Object> map);

    List<EamEquPointInspectionOrderDetDto> findHtList(Map<String, Object> map);

    /**
     * 批量新增点检单明细以及其履历表
     * @param items
     * @return
     */
    int batchSave(List<EamEquPointInspectionOrderDet> items);
}
