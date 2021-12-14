package com.fantechs.provider.wms.in.mapper;

import com.fantechs.common.base.general.dto.wms.in.WmsInHtInPlanOrderDetDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInHtInPlanOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInHtInPlanOrderDetMapper extends MyMapper<WmsInHtInPlanOrderDet> {
     List<WmsInHtInPlanOrderDetDto> findList(Map<String ,Object> map);
}