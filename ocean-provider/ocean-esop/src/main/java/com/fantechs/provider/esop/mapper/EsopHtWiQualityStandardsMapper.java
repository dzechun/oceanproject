package com.fantechs.provider.esop.mapper;

import com.fantechs.common.base.general.entity.esop.history.EsopHtWiQualityStandards;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EsopHtWiQualityStandardsMapper extends MyMapper<EsopHtWiQualityStandards> {
    List<EsopHtWiQualityStandards> findHtList(Map<String,Object> map);
}