package com.fantechs.provider.wms.out.mapper;

import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanStockListOrderDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPlanStockListOrder;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutPlanStockListOrder;
import com.fantechs.common.base.mybatis.MyMapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsOutPlanStockListOrderMapper extends MyMapper<WmsOutPlanStockListOrder> {
    List<WmsOutPlanStockListOrderDto> findList(SearchWmsOutPlanStockListOrder searchWmsOutPlanStockListOrder);
}