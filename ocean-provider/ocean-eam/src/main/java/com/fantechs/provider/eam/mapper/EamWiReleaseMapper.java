package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamWiReleaseDto;
import com.fantechs.common.base.general.entity.eam.EamWiRelease;
import com.fantechs.common.base.general.entity.eam.search.SearchEamWiRelease;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EamWiReleaseMapper extends MyMapper<EamWiRelease> {
    List<EamWiReleaseDto> findList(SearchEamWiRelease searchEamWiRelease);
}