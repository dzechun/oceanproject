package com.fantechs.provider.daq.mapper;

import com.fantechs.common.base.general.entity.eam.history.EamHtEquipment;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DaqHtEquipmentMapper extends MyMapper<EamHtEquipment> {
    List<EamHtEquipment> findHtList(Map<String,Object> map);
}