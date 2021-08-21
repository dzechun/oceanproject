package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentJigList;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamHtEquipmentJigListMapper extends MyMapper<EamHtEquipmentJigList> {
    List<EamHtEquipmentJigList> findList(Map<String,Object> map);
}