package com.fantechs.provider.wms.out.mapper;

import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtPlanDeliveryOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsOutHtPlanDeliveryOrderMapper extends MyMapper<WmsOutHtPlanDeliveryOrder> {
    List<WmsOutHtPlanDeliveryOrder> findHtList(Map<String, Object> map);
}