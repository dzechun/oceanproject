package com.fantechs.provider.om.mapper.sales;

import com.fantechs.common.base.general.dto.om.sales.OmSalesOrderDetDto;
import com.fantechs.common.base.general.entity.om.sales.OmSalesOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OmSalesOrderDetMapper extends MyMapper<OmSalesOrderDet> {
    List<OmSalesOrderDetDto> findList(Map<String, Object> map);

    List<OmSalesOrderDetDto> findHtList(Map<String, Object> map);
}