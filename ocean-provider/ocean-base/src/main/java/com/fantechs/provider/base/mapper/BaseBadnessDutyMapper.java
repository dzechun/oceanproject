package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseBadnessDutyDto;
import com.fantechs.common.base.general.entity.basic.BaseBadnessDuty;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseBadnessDutyMapper extends MyMapper<BaseBadnessDuty> {
    List<BaseBadnessDutyDto> findList(Map<String, Object> map);
}
