package com.fantechs.provider.wms.in.mapper;

import com.fantechs.common.base.general.dto.wms.in.WmsInPlanReceivingOrderDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInPlanReceivingOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInPlanReceivingOrderMapper extends MyMapper<WmsInPlanReceivingOrder> {
    List<WmsInPlanReceivingOrderDto> findList(Map<String,Object> map);
}