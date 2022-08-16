package com.fantechs.provider.om.mapper.ht;

import com.fantechs.common.base.general.dto.om.OmHtSalesOrderDto;
import com.fantechs.common.base.general.entity.om.OmHtSalesOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OmHtSalesOrderMapper extends MyMapper<OmHtSalesOrder> {
    List<OmHtSalesOrderDto> findList(Map<String, Object> map);

}