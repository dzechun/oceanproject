package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtLabel;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseLabel;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseHtLabelMapper extends MyMapper<BaseHtLabel> {
    List<BaseHtLabel> findList(Map<String, Object> map);
}