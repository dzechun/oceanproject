package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleDto;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRule;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRule;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface BaseBarcodeRuleMapper extends MyMapper<BaseBarcodeRule> {
    List<BaseBarcodeRuleDto> findList(Map<String, Object> map);
}