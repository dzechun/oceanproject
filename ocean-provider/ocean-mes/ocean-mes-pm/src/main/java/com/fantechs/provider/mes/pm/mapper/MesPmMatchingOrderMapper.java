package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.dto.mes.pm.MesPmMatchingOrderDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmMatchingOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MesPmMatchingOrderMapper extends MyMapper<MesPmMatchingOrder> {

    List<MesPmMatchingOrderDto> findList(Map<String, Object> map);
}