package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.dto.basic.BaseBarcodeRuleSpecDto;
import com.fantechs.common.base.general.entity.basic.BaseBarcodeRuleSpec;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSpec;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BaseBarcodeRuleSpecMapper extends MyMapper<BaseBarcodeRuleSpec> {
    List<BaseBarcodeRuleSpecDto> findList(SearchBaseBarcodeRuleSpec searchBaseBarcodeRuleSpec);

    int updateBatch(List<BaseBarcodeRuleSpec> list);
}