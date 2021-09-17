package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.entity.eam.EamSparePartReEqu;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamSparePartReEquMapper extends MyMapper<EamSparePartReEqu> {
    List<EamSparePartReEqu> findList(Map<String,Object> map);
}