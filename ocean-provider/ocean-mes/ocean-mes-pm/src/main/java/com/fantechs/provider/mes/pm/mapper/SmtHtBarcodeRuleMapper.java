package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.entity.mes.pm.history.SmtHtBarcodeRule;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtBarcodeRule;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmtHtBarcodeRuleMapper extends MyMapper<SmtHtBarcodeRule> {
    List<SmtHtBarcodeRule> findList(SearchSmtBarcodeRule searchSmtBarcodeRule);
}