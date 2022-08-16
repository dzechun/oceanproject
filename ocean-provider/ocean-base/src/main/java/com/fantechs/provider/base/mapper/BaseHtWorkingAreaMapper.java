package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtWorkingArea;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseHtWorkingAreaMapper extends MyMapper<BaseHtWorkingArea> {
    List<BaseHtWorkingArea> findList(Map<String, Object> map);
}
