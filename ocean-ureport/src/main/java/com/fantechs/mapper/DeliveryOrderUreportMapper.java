package com.fantechs.mapper;

import com.fantechs.common.base.general.dto.wms.out.WmsOutDeliveryOrderDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutDeliveryOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface DeliveryOrderUreportMapper extends MyMapper<WmsOutDeliveryOrderDet> {

    List<WmsOutDeliveryOrderDto> findList(Map<String, Object> map);

}
