package com.fantechs.provider.guest.eng.mapper;

import com.fantechs.common.base.general.dto.eng.EngPackingOrderDto;
import com.fantechs.common.base.general.entity.eng.EngPackingOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EngPackingOrderMapper extends MyMapper<EngPackingOrder> {
    List<EngPackingOrderDto> findList(Map<String, Object> map);

    String findMaterialPurpose(Map<String,Object> map);
}