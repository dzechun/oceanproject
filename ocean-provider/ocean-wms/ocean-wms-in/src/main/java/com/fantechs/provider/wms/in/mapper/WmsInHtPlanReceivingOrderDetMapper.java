package com.fantechs.provider.wms.in.mapper;

import com.fantechs.common.base.general.dto.wms.in.WmsInHtPlanReceivingOrderDetDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInHtPlanReceivingOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInHtPlanReceivingOrderDetMapper extends MyMapper<WmsInHtPlanReceivingOrderDet> {
    List<WmsInHtPlanReceivingOrderDetDto> findHtList(Map<String ,Object> map);
}