package com.fantechs.provider.mes.sfc.mapper;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcRepairOrderDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcRepairOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesSfcRepairOrderMapper extends MyMapper<MesSfcRepairOrder> {
    List<MesSfcRepairOrderDto> findList(Map<String, Object> map);
}