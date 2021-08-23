package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamEquInspectionOrderDto;
import com.fantechs.common.base.general.dto.eam.EamEquPointInspectionOrderDto;
import com.fantechs.common.base.general.entity.eam.EamEquPointInspectionOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/08/20.
 */

public interface EamEquPointInspectionOrderService extends IService<EamEquPointInspectionOrder> {
    List<EamEquPointInspectionOrderDto> findList(Map<String, Object> map);

    List<EamEquPointInspectionOrderDto> findHtList(Map<String, Object> map);

    /**
     * 通过设备条码获取点检项目配置信息
     * @param map
     * @return
     */
    EamEquInspectionOrderDto findListForOrder(Map<String,Object> map);

    /**
     * 新增点检单以及点检单明细
     * @param eamEquPointInspectionOrder
     * @return
     */
    int save(EamEquPointInspectionOrder eamEquPointInspectionOrder);

    /**
     * 修改点检单以及点检单明细
     * @param eamEquPointInspectionOrder
     * @return
     */
    int update(EamEquPointInspectionOrder eamEquPointInspectionOrder);

    /**
     * 删除点检单以及点检单明细
     * @param ids
     * @return
     */
    int batchDelete(String ids);
}
