package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentJig;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamHtEquipmentJigMapper extends MyMapper<EamHtEquipmentJig> {
    List<EamHtEquipmentJig> findList(Map<String,Object> map);
}