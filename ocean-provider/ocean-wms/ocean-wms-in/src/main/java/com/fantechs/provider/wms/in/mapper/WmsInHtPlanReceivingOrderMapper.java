package com.fantechs.provider.wms.in.mapper;

import com.fantechs.common.base.general.dto.wms.in.WmsInHtPlanReceivingOrderDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInHtPlanReceivingOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInHtPlanReceivingOrderMapper extends MyMapper<WmsInHtPlanReceivingOrder> {
    List<WmsInHtPlanReceivingOrderDto> findHtList(Map<String,Object> map);
}