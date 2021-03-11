package com.fantechs.provider.quartz.mapper;

import com.fantechs.common.base.general.dto.bcm.SmtProcessSchedulingDto;
import com.fantechs.common.base.general.entity.bcm.SmtProcessScheduling;
import com.fantechs.common.base.general.entity.bcm.search.SearchSmtProcessScheduling;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmtProcessSchedulingMapper extends MyMapper<SmtProcessScheduling> {
    List<SmtProcessSchedulingDto> findList(SearchSmtProcessScheduling searchSmtProcessScheduling);
}