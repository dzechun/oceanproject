package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.entity.eam.history.EamHtWiQualityStandards;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamHtWiQualityStandardsMapper extends MyMapper<EamHtWiQualityStandards> {
    List<EamHtWiQualityStandards> findHtList(Map<String,Object> map);
}