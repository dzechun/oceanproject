package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.BaseSampleProcess;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseSampleProcessMapper extends MyMapper<BaseSampleProcess> {
    List<BaseSampleProcess> findList(Map<String,Object> map);
}