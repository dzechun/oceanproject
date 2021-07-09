package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.entity.eam.EamWiBom;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamWiBomMapper extends MyMapper<EamWiBom> {
    List<EamWiBom> findList(Map<String,Object> map);
}