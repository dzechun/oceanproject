package com.fantechs.provider.esop.mapper;

import com.fantechs.common.base.general.entity.esop.EsopWiQualityStandards;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EsopWiQualityStandardsMapper extends MyMapper<EsopWiQualityStandards> {
    List<EsopWiQualityStandards> findList(Map<String,Object> map);
}