package com.fantechs.provider.wms.out.mapper;

import com.fantechs.common.base.general.dto.wms.out.WmsOutPurchaseReturnDto;
import com.fantechs.common.base.general.entity.wms.out.WmsOutPurchaseReturn;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface WmsOutPurchaseReturnMapper extends MyMapper<WmsOutPurchaseReturn> {
    List<WmsOutPurchaseReturnDto> findList(Map<String, Object> dynamicConditionByEntity);
}