package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamEquipmentMaintainOrderDto;
import com.fantechs.common.base.general.entity.eam.history.EamHtEquipmentMaintainOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamHtEquipmentMaintainOrderMapper extends MyMapper<EamHtEquipmentMaintainOrder> {
    List<EamEquipmentMaintainOrderDto> findList(Map<String, Object> map);
}