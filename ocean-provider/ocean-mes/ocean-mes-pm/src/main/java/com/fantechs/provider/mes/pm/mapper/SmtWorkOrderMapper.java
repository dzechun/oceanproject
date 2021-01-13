package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.dto.apply.SmtWorkOrderDto;
import com.fantechs.common.base.entity.apply.SmtWorkOrder;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrder;
import com.fantechs.common.base.entity.basic.SmtProductBomDet;
import com.fantechs.common.base.entity.basic.SmtRouteProcess;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SmtWorkOrderMapper extends MyMapper<SmtWorkOrder> {
    List<SmtWorkOrderDto> findList(SearchSmtWorkOrder searchSmtWorkOrder);

    List<SmtProductBomDet> selectProductBomDet(@Param(value = "materialId")Long materialId);

    SmtWorkOrderDto selectByWorkOrderId(@Param(value = "workOrderId")Long workOrderId);

    List<SmtRouteProcess> selectRouteProcessByRouteId(@Param(value = "routeId")Long routeId);

    List<SmtWorkOrderDto> pdaFindList(SearchSmtWorkOrder searchSmtWorkOrder);
}