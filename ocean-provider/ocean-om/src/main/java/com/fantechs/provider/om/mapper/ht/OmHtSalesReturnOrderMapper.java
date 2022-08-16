package com.fantechs.provider.om.mapper.ht;

import com.fantechs.common.base.general.dto.om.OmHtSalesReturnOrderDto;
import com.fantechs.common.base.general.entity.om.OmHtSalesReturnOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OmHtSalesReturnOrderMapper extends MyMapper<OmHtSalesReturnOrder> {
    List<OmHtSalesReturnOrderDto> findList(Map<String,Object>map);
}