package com.fantechs.provider.eam.mapper;

import com.fantechs.common.base.general.dto.eam.EamJigRepairOrderDto;
import com.fantechs.common.base.general.entity.eam.EamJigRepairOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EamJigRepairOrderMapper extends MyMapper<EamJigRepairOrder> {
    List<EamJigRepairOrderDto> findList(Map<String, Object> map);
}