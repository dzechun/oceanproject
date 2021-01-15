package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.entity.mes.pm.history.SmtHtBarcodeRuleSet;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtBarcodeRuleSet;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmtHtBarcodeRuleSetMapper extends MyMapper<SmtHtBarcodeRuleSet> {
    List<SmtHtBarcodeRuleSet> findList(SearchSmtBarcodeRuleSet searchSmtBarcodeRuleSet);
}