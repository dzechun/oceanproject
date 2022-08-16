package com.fantechs.provider.om.mapper;

import com.fantechs.common.base.general.dto.om.OmSalesReturnOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmSalesReturnOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OmSalesReturnOrderDetMapper extends MyMapper<OmSalesReturnOrderDet> {
    List<OmSalesReturnOrderDetDto> findList(Map<String,Object> map);

    String findUnitName(@Param("materialId")Long materialId);
}