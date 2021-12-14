package com.fantechs.provider.wms.in.mapper;

import com.fantechs.common.base.general.dto.wms.in.WmsInHtInPlanOrderDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInHtInPlanOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInHtInPlanOrderMapper extends MyMapper<WmsInHtInPlanOrder> {
        List<WmsInHtInPlanOrderDto> findList(Map<String ,Object> map);
}