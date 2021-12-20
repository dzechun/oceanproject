package com.fantechs.provider.om.mapper.ht;

import com.fantechs.common.base.general.dto.om.OmHtPurchaseOrderDto;
import com.fantechs.common.base.general.entity.om.OmHtPurchaseOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OmHtPurchaseOrderMapper extends MyMapper<OmHtPurchaseOrder> {
    List<OmHtPurchaseOrderDto> findList(Map<String, Object> map);
}