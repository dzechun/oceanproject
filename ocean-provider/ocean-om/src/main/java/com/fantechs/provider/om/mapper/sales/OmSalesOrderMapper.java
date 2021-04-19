package com.fantechs.provider.om.mapper.sales;

import com.fantechs.common.base.general.dto.om.sales.OmSalesOrderDto;
import com.fantechs.common.base.general.entity.om.sales.OmSalesOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OmSalesOrderMapper extends MyMapper<OmSalesOrder> {
    List<OmSalesOrderDto> findList(Map<String, Object> map);

    List<OmSalesOrderDto> findHtList(Map<String, Object> map);
}