package com.fantechs.provider.quartz.mapper;

import com.fantechs.common.base.general.dto.basic.BaseProcessSchedulingDto;
import com.fantechs.common.base.general.entity.basic.BaseProcessScheduling;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseProcessScheduling;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmtProcessSchedulingMapper extends MyMapper<BaseProcessScheduling> {
    List<BaseProcessSchedulingDto> findList(SearchBaseProcessScheduling searchBaseProcessScheduling);
}