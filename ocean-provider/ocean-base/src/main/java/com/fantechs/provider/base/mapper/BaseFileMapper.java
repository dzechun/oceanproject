package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseFile;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseFileMapper extends MyMapper<BaseFile> {
    List<BaseFile> findList(Map<String, Object> map);
}