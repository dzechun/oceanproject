package com.fantechs.provider.wms.out.mapper;


import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtDeliveryOrder;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtDeliveryOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsOutHtDeliveryOrderDetMapper extends MyMapper<WmsOutHtDeliveryOrderDet> {
    List<WmsOutHtDeliveryOrderDet> findHtList(Map<String, Object> map);
}