package com.fantechs.provider.om.mapper;

import com.fantechs.common.base.general.dto.om.OmPurchaseOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmPurchaseOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OmPurchaseOrderDetMapper extends MyMapper<OmPurchaseOrderDet> {
    List<OmPurchaseOrderDetDto> findList(Map<String, Object> map);
}