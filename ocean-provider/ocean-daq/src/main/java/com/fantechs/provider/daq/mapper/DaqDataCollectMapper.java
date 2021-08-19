package com.fantechs.provider.daq.mapper;

import com.fantechs.common.base.general.dto.daq.DaqDataCollectDto;
import com.fantechs.common.base.general.entity.daq.DaqDataCollect;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DaqDataCollectMapper extends MyMapper<DaqDataCollect> {
    List<DaqDataCollectDto> findList(Map<String, Object> map);
    List<DaqDataCollectDto> findByEquipmentId(@Param(value="equipmentId") Long equipmentId);
    String findByGroup(@Param(value="equipmentDataGroupId") Long equipmentDataGroupId);
}