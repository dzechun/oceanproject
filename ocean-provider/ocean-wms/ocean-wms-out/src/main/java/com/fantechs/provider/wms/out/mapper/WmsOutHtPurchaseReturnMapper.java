package com.fantechs.provider.wms.out.mapper;

import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtPurchaseReturn;
import com.fantechs.common.base.mybatis.MyMapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsOutHtPurchaseReturnMapper extends MyMapper<WmsOutHtPurchaseReturn> {
    List<WmsOutHtPurchaseReturn> findHTList(Map<String, Object> dynamicConditionByEntity);
}