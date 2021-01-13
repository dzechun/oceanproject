package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.entity.apply.history.SmtHtBarcodeRuleSet;
import com.fantechs.common.base.entity.apply.search.SearchSmtBarcodeRuleSet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmtHtBarcodeRuleSetMapper extends MyMapper<SmtHtBarcodeRuleSet> {
    List<SmtHtBarcodeRuleSet> findList(SearchSmtBarcodeRuleSet searchSmtBarcodeRuleSet);
}