package com.fantechs.provider.wms.out.mapper;

import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtDeliveryReqOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsOutHtDeliveryReqOrderMapper extends MyMapper<WmsOutHtDeliveryReqOrder> {
    List<WmsOutHtDeliveryReqOrder> findHtList(Map<String, Object> map);
}