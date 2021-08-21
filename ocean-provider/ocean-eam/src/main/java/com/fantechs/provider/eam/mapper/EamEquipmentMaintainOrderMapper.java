package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamEquipmentMaintainOrderDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentMaintainOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamEquipmentMaintainOrderMapper extends MyMapper<EamEquipmentMaintainOrder> {
    List<EamEquipmentMaintainOrderDto> findList(Map<String, Object> map);
}