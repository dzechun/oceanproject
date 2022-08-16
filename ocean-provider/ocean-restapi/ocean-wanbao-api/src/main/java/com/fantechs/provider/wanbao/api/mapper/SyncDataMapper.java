package com.fantechs.provider.wanbao.api.mapper;

import com.fantechs.common.base.general.dto.om.OmSalesOrderDetDto;
import com.fantechs.common.base.general.entity.mes.pm.MesPmWorkOrder;
import com.fantechs.common.base.general.entity.om.OmSalesOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface SyncDataMapper extends MyMapper<OmSalesOrder> {

    List<OmSalesOrder> findAllSalesOrder(Map<String, Object> map);

    List<OmSalesOrderDetDto> findAllSalesorderDet(Map<String, Object> map);

    List<MesPmWorkOrder> findAllWorkOrder(Map<String, Object> map);
}