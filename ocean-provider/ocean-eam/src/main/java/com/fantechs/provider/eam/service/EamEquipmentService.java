package com.fantechs.provider.eam.service;

import com.fantechs.common.base.general.dto.eam.EamEquInspectionOrderDto;
import com.fantechs.common.base.general.dto.eam.EamEquMaintainOrderDto;
import com.fantechs.common.base.general.dto.eam.EamEquipmentDto;
import com.fantechs.common.base.general.dto.eam.imports.EamEquipmentImport;
import com.fantechs.common.base.general.entity.eam.EamEquipment;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipment;
import com.fantechs.common.base.support.IService;

import java.util.List;
import java.util.Map;

/**
 *
 * Created by leifengzhi on 2021/06/25.
 */

public interface EamEquipmentService extends IService<EamEquipment> {
    List<EamEquipmentDto> findList(Map<String, Object> map);
    List<EamHtEquipment> findHtList(Map<String, Object> map);

    int batchUpdate(List<EamEquipment> list);

    EamEquipment detailByIp(String ip);

    List<EamEquipmentDto> findNoGroup(Map<String, Object> map);

    /**
     * 通过设备条码获取点检项目配置信息
     * @param map
     * @return
     */
    List<EamEquInspectionOrderDto> findListForInspectionOrder(Map<String, Object> map);

    /**
     * 通过设备条码获取保养项目配置信息
     * @param map
     * @return
     */
    List<EamEquMaintainOrderDto> findListForMaintainOrder(Map<String,Object> map);

    Map<String, Object> importExcel(List<EamEquipmentImport> eamEquipmentImports);
}
