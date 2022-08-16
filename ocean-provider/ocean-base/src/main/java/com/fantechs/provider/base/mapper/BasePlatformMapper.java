package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BasePlatform;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BasePlatformMapper extends MyMapper<BasePlatform> {
    List<BasePlatform> findList(Map<String, Object> map);
}