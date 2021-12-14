package com.fantechs.provider.wms.in.mapper;

import com.fantechs.common.base.general.dto.wms.in.WmsInPlanReceivingOrderDetDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInPlanReceivingOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInPlanReceivingOrderDetMapper extends MyMapper<WmsInPlanReceivingOrderDet> {
    List<WmsInPlanReceivingOrderDetDto> findList(Map<String,Object> map);
}