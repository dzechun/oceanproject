package com.fantechs.provider.om.mapper;

import com.fantechs.common.base.general.dto.om.OmSalesOrderDto;
import com.fantechs.common.base.general.entity.om.OmSalesOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OmSalesOrderMapper extends MyMapper<OmSalesOrder> {
    List<OmSalesOrderDto> findList(Map<String, Object> map);
    List<OmSalesOrderDto> findAll(Map<String, Object> map);
    int batchUpdate(List<OmSalesOrder> orders);
}