package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentParam;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentParamList;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamHtEquipmentParamMapper extends MyMapper<EamHtEquipmentParam> {
    List<EamHtEquipmentParam> findHtList(Map<String,Object> map);
}