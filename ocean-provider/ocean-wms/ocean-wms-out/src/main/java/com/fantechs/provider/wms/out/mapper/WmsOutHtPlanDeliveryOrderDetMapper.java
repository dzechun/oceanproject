package com.fantechs.provider.wms.out.mapper;

import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtPlanDeliveryOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsOutHtPlanDeliveryOrderDetMapper extends MyMapper<WmsOutHtPlanDeliveryOrderDet> {
    List<WmsOutHtPlanDeliveryOrderDet> findHtList(Map<String, Object> map);
}