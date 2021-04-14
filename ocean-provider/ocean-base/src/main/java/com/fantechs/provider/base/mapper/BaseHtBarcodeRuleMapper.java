package com.fantechs.provider.base.mapper;

import com.fantechs.common.base.general.entity.basic.history.BaseHtBarcodeRule;
import com.fantechs.common.base.general.entity.basic.search.SearchBaseBarcodeRule;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BaseHtBarcodeRuleMapper extends MyMapper<BaseHtBarcodeRule> {
    List<BaseHtBarcodeRule> findList(SearchBaseBarcodeRule searchBaseBarcodeRule);
}