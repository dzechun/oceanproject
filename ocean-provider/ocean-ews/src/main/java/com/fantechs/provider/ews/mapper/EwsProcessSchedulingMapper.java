package com.fantechs.provider.ews.mapper;

import com.fantechs.common.base.general.dto.ews.EwsProcessSchedulingDto;
import com.fantechs.common.base.general.entity.ews.EwsProcessScheduling;
import com.fantechs.common.base.general.entity.ews.search.SearchEwsProcessScheduling;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EwsProcessSchedulingMapper extends MyMapper<EwsProcessScheduling> {
    List<EwsProcessSchedulingDto> findList(SearchEwsProcessScheduling searchEwsProcessScheduling);
}