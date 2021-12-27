package com.fantechs.provider.om.mapper.ht;

import com.fantechs.common.base.general.dto.om.OmPurchaseOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmHtPurchaseOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OmHtPurchaseOrderDetMapper extends MyMapper<OmHtPurchaseOrderDet> {
    List<OmPurchaseOrderDetDto> findList(Map<String, Object> map);
}