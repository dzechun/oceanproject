package com.fantechs.provider.mes.sfc.mapper;

import com.fantechs.common.base.general.entity.mes.sfc.history.MesSfcHtRepairOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesSfcHtRepairOrderMapper extends MyMapper<MesSfcHtRepairOrder> {
    List<MesSfcHtRepairOrder> findHtList(Map<String, Object> map);
}