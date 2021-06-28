package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.entity.eam.history.EamHtEquipment;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentParamList;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamHtEquipmentParamListMapper extends MyMapper<EamHtEquipmentParamList> {
    List<EamHtEquipmentParamList> findHtList(Map<String,Object> map);
}