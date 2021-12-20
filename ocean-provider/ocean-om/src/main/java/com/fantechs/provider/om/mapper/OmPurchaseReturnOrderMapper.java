package com.fantechs.provider.om.mapper;

import com.fantechs.common.base.general.dto.om.OmPurchaseReturnOrderDto;
import com.fantechs.common.base.general.entity.om.OmPurchaseReturnOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OmPurchaseReturnOrderMapper extends MyMapper<OmPurchaseReturnOrder> {
    List<OmPurchaseReturnOrderDto> findList(Map<String, Object> map);
}