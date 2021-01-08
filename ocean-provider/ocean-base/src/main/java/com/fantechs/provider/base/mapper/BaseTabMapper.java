package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseTab;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseTabMapper extends MyMapper<BaseTab> {

    List<BaseTab> findList(Map<String, Object> map);
}