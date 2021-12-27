package com.fantechs.provider.om.mapper.ht;

import com.fantechs.common.base.general.entity.om.OmHtPurchaseReturnOrder;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface OmHtPurchaseReturnOrderMapper extends MyMapper<OmHtPurchaseReturnOrder> {
    List<OmHtPurchaseReturnOrder> findHtList(Map<String, Object> map);
}