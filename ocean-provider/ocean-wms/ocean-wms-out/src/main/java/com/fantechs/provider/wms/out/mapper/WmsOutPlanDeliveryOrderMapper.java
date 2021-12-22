package com.fantechs.provider.wms.out.mapper;

import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanDeliveryOrderDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPlanDeliveryOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsOutPlanDeliveryOrderMapper extends MyMapper<WmsOutPlanDeliveryOrder> {
    List<WmsOutPlanDeliveryOrderDto> findList(Map<String, Object> map);
}