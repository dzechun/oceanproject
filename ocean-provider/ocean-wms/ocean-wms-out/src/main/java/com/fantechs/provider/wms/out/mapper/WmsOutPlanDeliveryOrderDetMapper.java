package com.fantechs.provider.wms.out.mapper;

import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanDeliveryOrderDetDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPlanDeliveryOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsOutPlanDeliveryOrderDetMapper extends MyMapper<WmsOutPlanDeliveryOrderDet> {
    List<WmsOutPlanDeliveryOrderDetDto> findList(Map<String, Object> map);

    int batchUpdate(List<WmsOutPlanDeliveryOrderDetDto> wmsOutPlanDeliveryOrderDetDtos);
}