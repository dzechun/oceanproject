package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtBadnessCategory;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseHtBadnessCategoryMapper extends MyMapper<BaseHtBadnessCategory> {
    List<BaseHtBadnessCategory> findList(Map<String, Object> map);
}
