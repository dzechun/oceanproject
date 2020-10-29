package com.fantechs.provider.imes.apply.mapper;

import com.fantechs.common.base.entity.apply.history.SmtHtBarcodeRule;
import com.fantechs.common.base.entity.apply.search.SearchSmtBarcodeRule;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmtHtBarcodeRuleMapper extends MyMapper<SmtHtBarcodeRule> {
    List<SmtHtBarcodeRule> findList(SearchSmtBarcodeRule searchSmtBarcodeRule);
}