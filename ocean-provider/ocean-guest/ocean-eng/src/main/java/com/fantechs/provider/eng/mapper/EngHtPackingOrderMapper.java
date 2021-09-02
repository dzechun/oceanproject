package com.fantechs.provider.eng.mapper;

import com.fantechs.common.base.general.dto.eng.EngHtPackingOrderDto;
import com.fantechs.common.base.general.entity.eng.history.EngHtPackingOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EngHtPackingOrderMapper extends MyMapper<EngHtPackingOrder> {
    List<EngHtPackingOrderDto> findList(Map<String, Object> map);
}