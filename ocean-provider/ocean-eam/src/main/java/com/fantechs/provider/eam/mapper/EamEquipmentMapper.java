package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamEquInspectionOrderDto;
import com.fantechs.common.base.general.dto.eam.EamEquMaintainOrderDto;
import com.fantechs.common.base.general.dto.eam.EamEquipmentDto;
import com.fantechs.common.base.general.entity.eam.EamEquipment;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamEquipmentMapper extends MyMapper<EamEquipment> {
    List<EamEquipmentDto> findList(Map<String,Object> map);

    int batchUpdate(List<EamEquipment> list);

    List<EamEquipmentDto> findNoGroup(Map<String,Object> map);

    /**
     * 通过设备条码获取点检项目配置信息
     * @param map
     * @return
     */
    List<EamEquInspectionOrderDto> findListForInspectionOrder(Map<String,Object> map);

    /**
     * 通过设备条码获取保养项目配置信息
     * @param map
     * @return
     */
    List<EamEquMaintainOrderDto> findListForMaintainOrder(Map<String,Object> map);
}