package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseInAndOutRuleDto;
import com.fantechs.common.base.general.dto.basic.InOutParamsDto;
import com.fantechs.common.base.general.entity.basic.BaseInAndOutRule;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface BaseInAndOutRuleMapper extends MyMapper<BaseInAndOutRule> {
    List<BaseInAndOutRuleDto> findList(Map<String,Object> map);

    List<InOutParamsDto> findInOutParamMode(@Param("specificName")String specificName);

    List<String> findView(String category);

    Long inRuleExecute(String procName,Long warehouseId, Long materialId, BigDecimal qty);

    List<String> outRuleExecute(String procName,Long warehouseId,Long storageId, Long materialId, int qty);
}