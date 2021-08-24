package com.fantechs.provider.esop.mapper;

import com.fantechs.common.base.general.entity.esop.EsopWiBom;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EsopWiBomMapper extends MyMapper<EsopWiBom> {
    List<EsopWiBom> findList(Map<String,Object> map);
}