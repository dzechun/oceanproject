package com.fantechs.provider.wms.out.mapper;

import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtPlanDeliveryOrder;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtPlanStockListOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsOutHtPlanStockListOrderDetMapper extends MyMapper<WmsOutHtPlanStockListOrderDet> {
    List<WmsOutHtPlanStockListOrderDet> findHtList(Map<String, Object> map);
}