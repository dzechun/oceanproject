package com.fantechs.provider.om.mapper;

import com.fantechs.common.base.general.dto.om.OmPurchaseReturnOrderDetDto;
import com.fantechs.common.base.general.entity.om.OmPurchaseReturnOrderDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OmPurchaseReturnOrderDetMapper extends MyMapper<OmPurchaseReturnOrderDet> {
    List<OmPurchaseReturnOrderDetDto> findList(Map<String, Object> map);
    int batchUpdate(List<OmPurchaseReturnOrderDetDto> omPurchaseReturnOrderDetDtos);
}