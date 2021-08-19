package com.fantechs.provider.daq.mapper;

import com.fantechs.common.base.general.dto.eam.EamEquipmentDto;
import com.fantechs.common.base.general.entity.eam.EamEquipment;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DaqEquipmentMapper extends MyMapper<EamEquipment> {
    List<EamEquipmentDto> findList(Map<String,Object> map);

    int batchUpdate(List<EamEquipment> list);

    List<EamEquipmentDto> findNoGroup(Map<String,Object> map);
}