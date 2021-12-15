package com.fantechs.provider.wms.in.mapper;

import com.fantechs.common.base.general.dto.wms.in.WmsInHtReceivingOrderDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInHtReceivingOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInHtReceivingOrderMapper extends MyMapper<WmsInHtReceivingOrder> {
    List<WmsInHtReceivingOrderDto> findHtList(Map<String,Object> map);
}