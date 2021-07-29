package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamDataCollectDto;
import com.fantechs.common.base.general.entity.eam.EamDataCollect;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamDataCollectMapper extends MyMapper<EamDataCollect> {
    List<EamDataCollectDto> findList(Map<String, Object> map);

    List<EamDataCollectDto> findByGroup(@Param(value="equipmentId") Long equipmentId);
}