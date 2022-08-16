package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseProductYieldDto;
import com.fantechs.common.base.general.entity.basic.BaseProductYield;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseProductYieldMapper extends MyMapper<BaseProductYield> {
    List<BaseProductYieldDto> findList(Map<String, Object> map);
}