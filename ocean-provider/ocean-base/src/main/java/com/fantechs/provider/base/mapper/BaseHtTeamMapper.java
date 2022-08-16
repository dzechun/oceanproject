package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseTeamDto;
import com.fantechs.common.base.general.entity.basic.history.BaseHtTeam;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseHtTeamMapper extends MyMapper<BaseHtTeam> {

    List<BaseHtTeam> findHtList(Map<String, Object> map);
}