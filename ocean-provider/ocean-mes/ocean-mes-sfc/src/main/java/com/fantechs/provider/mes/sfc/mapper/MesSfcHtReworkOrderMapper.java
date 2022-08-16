package com.fantechs.provider.mes.sfc.mapper;

import com.fantechs.common.base.general.dto.mes.sfc.MesSfcHtReworkOrderDto;
import com.fantechs.common.base.general.entity.mes.sfc.MesSfcHtReworkOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesSfcHtReworkOrderMapper extends MyMapper<MesSfcHtReworkOrder> {
    List<MesSfcHtReworkOrderDto> findList(Map<String, Object> map);
}