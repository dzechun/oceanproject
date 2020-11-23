package com.fantechs.provider.imes.apply.mapper;

import com.fantechs.common.base.dto.apply.SmtWorkOrderDto;
import com.fantechs.common.base.entity.apply.SmtWorkOrder;
import com.fantechs.common.base.entity.apply.search.SearchSmtWorkOrder;
import com.fantechs.common.base.entity.basic.SmtProductBomDet;
import com.fantechs.common.base.entity.basic.SmtRouteProcess;
import com.fantechs.common.base.mybatis.MyMapper;

import java.util.List;

public interface SmtWorkOrderMapper extends MyMapper<SmtWorkOrder> {
    List<SmtWorkOrderDto> findList(SearchSmtWorkOrder searchSmtWorkOrder);

    List<SmtProductBomDet> selectProductBomDet(Long materialId);

    SmtWorkOrderDto selectByWorkOrderId(Long workOrderId);

    List<SmtRouteProcess> selectRouteProcessByRouteId(Long routeId);
}