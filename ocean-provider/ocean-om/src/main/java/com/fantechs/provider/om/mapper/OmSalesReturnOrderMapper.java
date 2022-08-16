package com.fantechs.provider.om.mapper;

import com.fantechs.common.base.general.dto.om.OmSalesReturnOrderDto;
import com.fantechs.common.base.general.entity.om.OmSalesReturnOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OmSalesReturnOrderMapper extends MyMapper<OmSalesReturnOrder> {
    List<OmSalesReturnOrderDto> findList(Map<String,Object> map);

    OmSalesReturnOrder findMaterial(@Param("materialId")Long materialId);
}