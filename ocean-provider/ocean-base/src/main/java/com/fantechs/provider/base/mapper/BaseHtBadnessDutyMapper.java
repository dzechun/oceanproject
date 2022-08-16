package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtBadnessDuty;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseHtBadnessDutyMapper extends MyMapper<BaseHtBadnessDuty> {
    List<BaseHtBadnessDuty> findList(Map<String, Object> map);
}
