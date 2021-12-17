package com.fantechs.provider.om.mapper;

import com.fantechs.common.base.general.dto.om.OmSalesOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmSalesOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OmSalesOrderDetMapper extends MyMapper<OmSalesOrderDet> {
    List<OmSalesOrderDetDto> findList(Map<String, Object> map);

    int batchUpdate(List<OmSalesOrderDetDto> omSalesOrderDetDtos);
}