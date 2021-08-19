package com.fantechs.provider.daq.mapper;

import com.fantechs.common.base.general.dto.daq.DaqEquipmentDto;
import com.fantechs.common.base.general.entity.daq.DaqEquipment;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DaqEquipmentMapper extends MyMapper<DaqEquipment> {
    List<DaqEquipmentDto> findList(Map<String,Object> map);

    int batchUpdate(List<DaqEquipment> list);

    List<DaqEquipmentDto> findNoGroup(Map<String,Object> map);
}