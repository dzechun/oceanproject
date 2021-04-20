package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.dto.mes.pm.MesPmWorkOrderDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchMesPmWorkOrder;
import com.fantechs.common.base.general.entity.basic.BaseProductBomDet;
import com.fantechs.common.base.general.entity.basic.BaseRouteProcess;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MesPmWorkOrderMapper extends MyMapper<MesPmWorkOrder> {
    List<MesPmWorkOrderDto> findList(SearchMesPmWorkOrder searchMesPmWorkOrder);

    List<BaseProductBomDet> selectProductBomDet(@Param(value = "materialId")Long materialId);

    MesPmWorkOrderDto selectByWorkOrderId(@Param(value = "workOrderId")Long workOrderId);

    List<BaseRouteProcess> selectRouteProcessByRouteId(@Param(value = "routeId")Long routeId);

    List<MesPmWorkOrderDto> pdaFindList(SearchMesPmWorkOrder searchMesPmWorkOrder);
}