package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamHtWiReleaseDetDto;
import com.fantechs.common.base.general.dto.eam.EamWiReleaseDetDto;
import com.fantechs.common.base.general.entity.eam.history.EamHtWiReleaseDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamHtWiReleaseDetMapper extends MyMapper<EamHtWiReleaseDet> {
    List<EamHtWiReleaseDetDto> findHtList(Map<String,Object> map);
}