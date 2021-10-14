package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtPlateParts;
import com.fantechs.common.base.general.entity.basic.history.BaseHtPlatform;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseHtPlatformMapper extends MyMapper<BaseHtPlatform> {
    List<BaseHtPlatform> findHtList(Map<String, Object> map);
}