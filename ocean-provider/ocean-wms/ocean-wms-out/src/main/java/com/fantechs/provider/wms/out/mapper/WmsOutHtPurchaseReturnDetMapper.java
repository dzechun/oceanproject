package com.fantechs.provider.wms.out.mapper;

import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtPurchaseReturnDet;
import com.fantechs.common.base.mybatis.MyMapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsOutHtPurchaseReturnDetMapper extends MyMapper<WmsOutHtPurchaseReturnDet> {
    List<WmsOutHtPurchaseReturnDet> findHTList(Map<String, Object> dynamicConditionByEntity);
}