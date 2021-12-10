package com.fantechs.provider.wms.in.mapper;

import com.fantechs.common.base.general.dto.wms.in.WmsInInPlanOrderDto;
import com.fantechs.common.base.general.entity.wms.in.WmsInInPlanOrder;
import com.fantechs.common.base.general.entity.wms.in.search.SearchWmsInInPlanOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsInInPlanOrderMapper extends MyMapper<WmsInInPlanOrder> {
    List<WmsInInPlanOrderDto> findList(Map<String ,Object> map);
}