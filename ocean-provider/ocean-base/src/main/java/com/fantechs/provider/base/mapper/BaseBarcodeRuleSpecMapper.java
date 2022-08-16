package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleSpecDto;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSpec;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseBarcodeRuleSpecMapper extends MyMapper<BaseBarcodeRuleSpec> {
    List<BaseBarcodeRuleSpecDto> findList(Map<String, Object> map);

    int updateBatch(List<BaseBarcodeRuleSpec> list);

    @Select("select information_schema.routines.SPECIFIC_NAME from information_schema.routines where routine_schema='fantech_imes_v2' and routine_type = 'FUNCTION' AND information_schema.routines.SPECIFIC_NAME LIKE CONCAT('BARCODE','%')")
    List<String> findFunction();

    String executeFunction(@Param("functionName")String functionName, @Param("params")String params);
}