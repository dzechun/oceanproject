package com.fantechs.provider.wms.out.mapper;

import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtDeliveryReqOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsOutHtDeliveryReqOrderDetMapper extends MyMapper<WmsOutHtDeliveryReqOrderDet> {
    List<WmsOutHtDeliveryReqOrderDet> findHtList(Map<String, Object> map);
}