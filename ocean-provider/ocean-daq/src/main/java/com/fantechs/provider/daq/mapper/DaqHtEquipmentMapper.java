package com.fantechs.provider.daq.mapper;

import com.fantechs.common.base.general.entity.daq.DaqHtEquipment;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DaqHtEquipmentMapper extends MyMapper<DaqHtEquipment> {
    List<DaqHtEquipment> findHtList(Map<String,Object> map);
}