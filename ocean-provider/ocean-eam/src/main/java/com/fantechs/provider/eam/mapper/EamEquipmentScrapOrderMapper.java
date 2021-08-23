package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamEquipmentScrapOrderDto;
import com.fantechs.common.base.general.entity.eam.EamEquipmentScrapOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamEquipmentScrapOrderMapper extends MyMapper<EamEquipmentScrapOrder> {
    List<EamEquipmentScrapOrderDto> findList(Map<String,Object> map);
}