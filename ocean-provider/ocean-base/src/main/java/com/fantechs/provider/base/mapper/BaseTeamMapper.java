package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseTeamDto;
import com.fantechs.common.base.general.entity.basic.BaseTeam;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseTeamMapper extends MyMapper<BaseTeam> {

    List<BaseTeamDto> findList(Map<String, Object> map);
}