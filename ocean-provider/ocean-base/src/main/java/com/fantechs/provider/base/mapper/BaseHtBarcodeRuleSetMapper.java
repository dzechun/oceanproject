package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtBarcodeRuleSet;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRuleSet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BaseHtBarcodeRuleSetMapper extends MyMapper<BaseHtBarcodeRuleSet> {
    List<BaseHtBarcodeRuleSet> findList(SearchBaseBarcodeRuleSet searchBaseBarcodeRuleSet);
}