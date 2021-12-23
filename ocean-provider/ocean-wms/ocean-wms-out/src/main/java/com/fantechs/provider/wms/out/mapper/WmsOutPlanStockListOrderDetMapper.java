package com.fantechs.provider.wms.out.mapper;

import com.fantechs.common.base.general.dto.wms.out.WmsOutPlanStockListOrderDetDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPlanStockListOrderDet;
import com.fantechs.common.base.general.entity.wms.out.search.SearchWmsOutPlanStockListOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface WmsOutPlanStockListOrderDetMapper extends MyMapper<WmsOutPlanStockListOrderDet> {
    List<WmsOutPlanStockListOrderDetDto> findList(SearchWmsOutPlanStockListOrderDet searchWmsOutPlanStockListOrderDet);
}