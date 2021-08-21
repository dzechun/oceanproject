package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamEquipmentMaintainOrderDetDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaintainOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamEquipmentMaintainOrderDetMapper extends MyMapper<EamEquipmentMaintainOrderDet> {
    List<EamEquipmentMaintainOrderDetDto> findList(Map<String, Object> map);
}