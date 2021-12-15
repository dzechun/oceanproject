package com.fantechs.provider.wms.in.mapper;

import com.fantechs.common.base.general.dto.wms.in.WmsInHtReceivingOrderDetDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInHtReceivingOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInHtReceivingOrderDetMapper extends MyMapper<WmsInHtReceivingOrderDet> {
    List<WmsInHtReceivingOrderDetDto> findHtList(Map<String ,Object> map);
}