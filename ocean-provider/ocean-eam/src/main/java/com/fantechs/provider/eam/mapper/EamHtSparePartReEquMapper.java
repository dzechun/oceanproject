package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.entity.eam.history.EamHtSparePartReEqu;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamHtSparePartReEquMapper extends MyMapper<EamHtSparePartReEqu> {
    List<EamHtSparePartReEqu> findHtList(Map<String,Object> map);
}