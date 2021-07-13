package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamWiReleaseDetDto;
import com.fantechs.common.base.general.entity.eam.EamWiReleaseDet;
import com.fantechs.common.base.general.entity.eam.search.SearchEamWiReleaseDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EamWiReleaseDetMapper extends MyMapper<EamWiReleaseDet> {
    List<EamWiReleaseDetDto> findList(SearchEamWiReleaseDet searchEamWiReleaseDet);
}