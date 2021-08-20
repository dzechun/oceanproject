package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.entity.eam.history.EamHtEquRepairOrderReplacement;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamHtEquRepairOrderReplacementMapper extends MyMapper<EamHtEquRepairOrderReplacement> {
    List<EamHtEquRepairOrderReplacement> findList(Map<String,Object> map);
}