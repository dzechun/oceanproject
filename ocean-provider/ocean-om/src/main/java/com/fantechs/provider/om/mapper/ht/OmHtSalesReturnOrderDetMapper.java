package com.fantechs.provider.om.mapper.ht;

import com.fantechs.common.base.general.dto.om.OmHtSalesReturnOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmHtSalesReturnOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OmHtSalesReturnOrderDetMapper extends MyMapper<OmHtSalesReturnOrderDet> {
    List<OmHtSalesReturnOrderDetDto> findList(Map<String,Object>map);
}