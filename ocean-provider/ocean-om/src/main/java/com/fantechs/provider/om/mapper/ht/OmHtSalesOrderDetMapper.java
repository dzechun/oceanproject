package com.fantechs.provider.om.mapper.ht;

import com.fantechs.common.base.general.dto.om.OmHtSalesOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmHtSalesOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OmHtSalesOrderDetMapper extends MyMapper<OmHtSalesOrderDet> {
    List<OmHtSalesOrderDetDto> findList(Map<String, Object> map);
}