package com.fantechs.provider.guest.eng.mapper;

import com.fantechs.common.base.general.dto.eng.EngPurchaseReqOrderDto;
import com.fantechs.common.base.general.entity.eng.EngPurchaseReqOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface EngPurchaseReqOrderMapper extends MyMapper<EngPurchaseReqOrder> {
    List<EngPurchaseReqOrderDto> findList(Map<String, Object> map);
}