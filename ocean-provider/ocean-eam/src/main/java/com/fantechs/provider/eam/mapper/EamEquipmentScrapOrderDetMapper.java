package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamEquipmentScrapOrderDetDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentScrapOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamEquipmentScrapOrderDetMapper extends MyMapper<EamEquipmentScrapOrderDet> {
    List<EamEquipmentScrapOrderDetDto> findList(Map<String,Object> map);
}