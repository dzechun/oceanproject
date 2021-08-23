package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamEquMaintainOrderDto;
import com.fantechs.common.base.general.dto.eam.EamEquipmentMaintainOrderDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaintainOrder;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;
/**
 *
 * Created by leifengzhi on 2021/08/21.
 */

public interface EamEquipmentMaintainOrderService extends IService<EamEquipmentMaintainOrder> {
    List<EamEquipmentMaintainOrderDto> findList(Map<String, Object> map);
    List<EamEquipmentMaintainOrderDto> findHtList(Map<String, Object> map);

    /**
     * 通过设备条码获取点检项目配置信息
     * @param map
     * @return
     */
    EamEquMaintainOrderDto findListForOrder(Map<String,Object> map);

    /**
     * 新增保养单以及保养单明细
     * @param eamEquipmentMaintainOrder
     * @return
     */
    int save(EamEquipmentMaintainOrder eamEquipmentMaintainOrder);

    /**
     * 修改保养单以及保养单明细
     * @param eamEquipmentMaintainOrder
     * @return
     */
    int update(EamEquipmentMaintainOrder eamEquipmentMaintainOrder);

    /**
     * 删除保养单以及保养单明细
     * @param ids
     * @return
     */
    int batchDelete(String ids);
}
