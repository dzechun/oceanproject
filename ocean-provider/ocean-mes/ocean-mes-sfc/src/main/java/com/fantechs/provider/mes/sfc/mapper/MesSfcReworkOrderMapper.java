package com.fantechs.provider.mes.sfc.mapper;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcReworkOrderDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcReworkOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesSfcReworkOrderMapper extends MyMapper<MesSfcReworkOrder> {
    List<MesSfcReworkOrderDto> findList(Map<String, Object> map);

    MesSfcReworkOrderDto getFirstTodayReworkOrder();
}