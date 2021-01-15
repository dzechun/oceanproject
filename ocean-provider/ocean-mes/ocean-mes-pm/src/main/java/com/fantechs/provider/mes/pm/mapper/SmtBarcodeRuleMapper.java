package com.fantechs.provider.mes.pm.mapper;

import com.fantechs.common.base.general.dto.mes.pm.SmtBarcodeRuleDto;
import com.fantechs.common.base.general.entity.mes.pm.SmtBarcodeRule;
import com.fantechs.common.base.general.dto.mes.pm.search.SearchSmtBarcodeRule;
import com.fantechs.common.base.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SmtBarcodeRuleMapper extends MyMapper<SmtBarcodeRule> {
    List<SmtBarcodeRuleDto> findList(SearchSmtBarcodeRule searchSmtBarcodeRule);
}