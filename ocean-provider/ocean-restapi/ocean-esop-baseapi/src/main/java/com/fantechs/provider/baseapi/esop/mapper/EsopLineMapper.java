package com.fantechs.provider.baseapi.esop.mapper;

import com.fantechs.common.base.general.entity.restapi.esop.EsopLine;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EsopLineMapper extends MyMapper<EsopLine> {
    List<EsopLine> findList(Map<String,Object> map);
}