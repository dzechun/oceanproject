package com.fantechs.provider.wms.out.mapper;

import com.fantechs.common.base.general.dto.wms.out.WmsOutPurchaseReturnDetDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPurchaseReturnDet;
import com.fantechs.common.base.general.entity.wms.out.history.WmsOutHtPurchaseReturnDet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsOutPurchaseReturnDetMapper extends MyMapper<WmsOutPurchaseReturnDet> {
    List<WmsOutPurchaseReturnDetDto> findList(Map<String, Object> dynamicConditionByEntity);
}